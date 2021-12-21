package ru.congas.output;

import org.jline.terminal.Terminal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.fusesource.jansi.Ansi;
import ru.congas.CongasClient;
import ru.congas.pages.ErrorScreen;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Console renderer thread
 * @author Mr_Told
 */
public class RenderThread extends Thread {

    final String S_RST   = Ansi.ansi().reset().toString();
    final String S_ERASE = Ansi.ansi().eraseScreen().toString();
    final String S_NL    = Ansi.ansi().newline().toString();
    final String S_SCR1  = Ansi.ansi().scrollUp(1).toString();

    final Logger logger = LogManager.getLogger(RenderThread.class);
    final Terminal terminal;
    final PrintWriter out;

    private volatile Canvas canvas = null;

    private int minFps = 0, maxFps = 60, averageFps = 60;
    private int width, height;
    private boolean running = false;

    public RenderThread(Terminal terminal) {
        super("Renderer");
        this.terminal = terminal;
        this.out = terminal.writer();
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
                        if (canvas.getMultiplexer() == 1) render();
                        else renderMultiplexer();
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

        char[][] matrix = canvas.getMatrix();
        Ansi[][] colors = canvas.getColors().clone();
        LineBuilder lineB = new LineBuilder(matrix[0].length, out);

        if (CongasClient.isDebug())
            lineB.append(S_NL).print();

        Ansi prevC = null;
        char c;
        for (int line = 0; line < matrix.length; line++) {
            for (int ch = 0; ch < matrix[0].length; ch++) {
                if (prevC != colors[line][ch]) {
                    prevC = colors[line][ch];
                    lineB.append(prevC == null ? S_RST : prevC.toString());
                }

                c = matrix[line][ch];
                if (c == Character.MIN_VALUE) c = ' ';
                lineB.append(c);
            }
            lineB.append('\n');
            lineB.print();
        }

        lineB.append(S_RST);
        for (int i = 0; i < (height - matrix.length); i++)
            lineB.append('\n');

        if (canvas.eraseScreen()) out.write(S_ERASE);

        if (CongasClient.isDebug()) {
            lineB.append("FPS: ").append(averageFps).append('/').append(canvas.getFps())
                    .append(" (").append(minFps).append(" - ").append(maxFps).append(')');
        }
        lineB.print();
        lineB.end();
    }

    private void renderMultiplexer() throws Exception {
        if (canvas == null)
            throw new Exception("Canvas is null on rendering!");

        if (canvas.resetMatrix()) canvas.resetMatrices();
        canvas.updateCanvas();

        char[][] matrix = canvas.getMatrix();
        Ansi[][] colors = canvas.getColors().clone();
        int outRealHeight = matrix.length * canvas.getMultiplexer();
        int outRealWidth  = matrix[0].length * canvas.getMultiplexer();
        StringBuilder sb = new StringBuilder(outRealHeight * outRealWidth + height - outRealHeight + width - outRealWidth);
        StringBuilder lineSb = new StringBuilder(outRealWidth + 1);

        Ansi prevC = null;
        char c;
        for (int line = 0; line < matrix.length; line++) {
            lineSb.setLength(0);
            for (int ch = 0; ch < matrix[0].length; ch++) {
                if (prevC != colors[line][ch]) {
                    prevC = colors[line][ch];
                    lineSb.append(prevC == null ? S_RST : prevC.toString());
                }

                c = matrix[line][ch];
                if (c == Character.MIN_VALUE) c = ' ';

                for (int mc = 0; mc < canvas.getMultiplexer(); mc++)
                    lineSb.append(c);

            }
            lineSb.append('\n');
            for (int mc = 0; mc < canvas.getMultiplexer(); mc++)
                sb.append(lineSb);
        }

        sb.append(S_RST);
        for (int i = 0; i < (height - outRealHeight); i++)
            sb.append('\n');

        if (canvas.eraseScreen()) out.write(S_ERASE);

        if (CongasClient.isDebug()) {
            sb.append("FPS: ").append(averageFps).append('/').append(canvas.getFps())
                    .append(" (").append(minFps).append(" - ").append(maxFps).append(')');
            out.write(S_NL);
        }
        out.write(sb.toString());
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
