package ru.congas.output;

import org.jline.terminal.Terminal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.fusesource.jansi.Ansi;
import ru.congas.CongasClient;
import ru.congas.pages.ErrorScreen;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Console renderer thread
 * @author Mr_Told
 */
public class RenderThread extends Thread {

    final static Ansi A_RST   = Ansi.ansi().reset();
    final static Ansi A_ERASE = Ansi.ansi().eraseScreen();

    final Logger logger = LogManager.getLogger(RenderThread.class);
    final Terminal terminal;
    final OutputStream out;

    private volatile Canvas canvas = null;

    private int minFps = 0, maxFps = 60, averageFps = 60;
    private int width, height;
    private boolean running = false;

    public RenderThread(Terminal terminal) {
        super("Renderer");
        this.terminal = terminal;
        this.out = terminal.output();
        width = terminal.getWidth();
        height = terminal.getHeight();
    }

    /**
     * Thread main loop. Register terminal resize, call render, count fps
     */
    public void run() {
        if (running) return;
        running = true;

        while (CongasClient.isRunning()) {
            int nw, nh;         // new width and height
            int whc = 0;        // width-height time counter, used for check width and height every 200ms only
            int secondDelta = 0;
            int ticks = 0, minTickTime = 1000, maxTickTime = 0;
            try {
                long loopTimer; // timer for loop time (to keep target fps)
                while (CongasClient.isRunning()) {
                    loopTimer = System.currentTimeMillis();

                    if (secondDelta >= 1000) {
                        minFps = secondDelta / maxTickTime;
                        maxFps = secondDelta / minTickTime;
                        averageFps = 1000 * ticks / secondDelta;
                        minTickTime = secondDelta;
                        maxTickTime = 0;
                        ticks = 0;
                        secondDelta = 0;
                    }

                    if (whc > 200) {
                        nw = terminal.getWidth();
                        nh = terminal.getHeight() - 1;
                        if (nw != width || nh != height) resize(nw, nh);
                        whc = 0;
                    }

                    if (canvas.liveUpdate() || canvas.updateNeeded()) {
                        canvas.forceUpdate(false);
                        render();
                    }

                    loopTimer = System.currentTimeMillis() - loopTimer;
                    if (loopTimer < canvas.getLoopTime()) { //noinspection BusyWait
                        sleep(canvas.getLoopTime() - loopTimer);
                        loopTimer = canvas.getLoopTime();
                    }
                    whc += loopTimer;
                    secondDelta += loopTimer;
                    ticks++;
                    if (loopTimer > maxTickTime) maxTickTime = (int) loopTimer;
                    if (loopTimer < minTickTime) minTickTime = (int) loopTimer;
                }
            } catch (Exception e) {
                logger.fatal("Fatal error into Render Thread: ", e);
                CongasClient.openPage(new ErrorScreen("Fatal error into Render Thread ",
                        canvas == null ? "null" : canvas.getName()));
            }
        }
    }

    /**
     * Render frame with properties from canvas
     * @throws IOException if terminal print goes wrong
     */
    private void render() throws Exception {
        if (canvas == null)
            throw new Exception("Canvas is null on rendering!");

        if (canvas.resetMatrix()) canvas.resetMatrices();
        canvas.updateCanvas();

        int outRealHeight = canvas.getMatrix().length * canvas.getMultiplexer();
        int outRealWidth  = canvas.getMatrix()[0].length * canvas.getMultiplexer();
        StringBuilder sb = new StringBuilder(outRealHeight * outRealWidth + height - outRealHeight + width - outRealWidth);
        StringBuilder lineSb = new StringBuilder(outRealWidth + 1);

        Ansi prevC = null;
        char c;
        for (int line = 0; line < canvas.getMatrix().length; line++) {
            lineSb.setLength(0);
            for (int ch = 0; ch < canvas.getMatrix()[0].length; ch++) {
                if (prevC != canvas.getColors()[line][ch]) {
                    prevC = canvas.getColors()[line][ch];
                    lineSb.append(prevC == null ? A_RST.toString() : prevC.toString());
                }

                c = canvas.getMatrix()[line][ch];
                if (c == Character.MIN_VALUE) c = ' ';

                for (int mc = 0; mc < canvas.getMultiplexer(); mc++)
                    lineSb.append(c);

            }
            lineSb.append('\n');
            for (int mc = 0; mc < canvas.getMultiplexer(); mc++)
                sb.append(lineSb);
        }

        sb.append(A_RST.toString());
        for (int i = 0; i < (height - outRealHeight); i++)
            sb.append('\n');

        if (canvas.eraseScreen()) out.write(A_ERASE.toString().getBytes());

        if (CongasClient.isDebug()) {
            sb.append("FPS: ").append(averageFps).append('/').append(canvas.getFps())
                    .append(" (").append(minFps).append(" - ").append(maxFps).append(')');
            out.write("\n".getBytes());
        }
        out.write(sb.toString().getBytes());
    }

    /**
     * Called when terminal resize detected
     * @param w new width
     * @param h new height
     */
    private void resize(int w, int h) {
        if (CongasClient.isDebug()) logger.info("Terminal resized from " + width + "x" + height + " to " + w + "x" + h);
        width = w;
        height = h;
        if (canvas != null)
            canvas.updateTerminal(w, h);
    }

    /**
     * Set canvas to render
     * @param c canvas
     */
    public void setCanvas(Canvas c) {
        if (CongasClient.isDebug()) logger.info("Canvas set to " + c.getName());
        this.canvas = c;
        canvas.updateTerminal(width, height);
    }

}
