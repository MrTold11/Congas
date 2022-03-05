package ru.congas.core.output;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jline.terminal.Terminal;
import org.jline.terminal.impl.AbstractTerminal;
import ru.congas.core.CongasCore;
import ru.congas.core.output.canvas.Frame;

import java.io.PrintWriter;

/**
 * Console renderer thread
 * @author Mr_Told
 */
public class RenderThread extends Thread {

    final String S_NL        = System.getProperty("line.separator");
    final String OUT_PREPARE = S_NL + ((char) 27) + "[E";

    final Logger logger = LogManager.getLogger(RenderThread.class);
    final AbstractTerminal terminal;
    final CongasCore core;
    final PrintWriter out;

    private volatile ScreenBuffer screen = null;
    private volatile boolean updateScreenSize = false;

    //fps counters
    private int minFps = 0, maxFps = 60, averageFps = 60;

    private static volatile int width = 1, height = 1;
    private boolean running = false;

    public RenderThread(CongasCore core, Terminal terminal) {
        super("Renderer");
        this.core = core;
        this.terminal = (AbstractTerminal) terminal;
        this.out = terminal.writer();
        width = terminal.getWidth();
        height = terminal.getHeight();
    }

    /**
     * Thread main loop. Register terminal resize, call render, count fps
     */
    public final void run() {
        if (running) return;
        running = true;

        while (CongasCore.isRunning()) {
            //noinspection StatementWithEmptyBody
            while (screen == null);
            int nw, nh;         // new width and height
            int whc = 0;        // width-height time counter, used for check width and height every 200ms only
            int secondDelta = 0;
            int ticks = 0, minTickTime = 1000, maxTickTime = 0;
            try {
                long loopTimer; // timer for loop time (to keep target fps)
                while (CongasCore.isRunning()) {
                    loopTimer = System.currentTimeMillis();

                    if (secondDelta >= 1000) {
                        if (maxTickTime < 1) maxTickTime = 1;
                        if (minTickTime < 1) minTickTime = 1;
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
                        if (nw != width || nh != height || updateScreenSize) {
                            updateScreenSize = false;
                            resize(nw, nh);
                        }
                        whc = 0;
                    }

                    if (screen.liveUpdate() || screen.updateNeeded())
                        render(screen.getFrame(this));

                    loopTimer = System.currentTimeMillis() - loopTimer;
                    if (loopTimer < screen.getLoopTime()) { //noinspection BusyWait
                        sleep(screen.getLoopTime() - loopTimer);
                        loopTimer = screen.getLoopTime();
                    }
                    whc += loopTimer;
                    secondDelta += loopTimer;
                    ticks++;
                    if (loopTimer > maxTickTime) maxTickTime = (int) loopTimer;
                    if (loopTimer < minTickTime) minTickTime = (int) loopTimer;
                }
            } catch (Exception e) {
                core.handleSystemThreadException("Fatal error into Render Thread", "", e);
            }
        }
    }

    private void render(Frame frame) {
        if (frame == null) {
            return;
        }
        Object[] data = frame.get();
        if (data == null) {
            return;
        }

        int matrixHeight = (int) data[1];
        int matrixWidth = (int) data[2];
        StringBuilder sb = new StringBuilder(matrixHeight * matrixWidth + height - matrixHeight + width - matrixWidth);

        sb.append(OUT_PREPARE);
        sb.append((String) data[0]);
        sb.append((char) 27).append("[m");
        for (int i = 0; i < (height - matrixHeight); i++)
            sb.append(S_NL);

        if (CongasCore.isDebug()) {
            sb.append("FPS: ").append(averageFps).append('/').append(screen.getFps())
                    .append(" (").append(minFps).append(" - ").append(maxFps).append(')');
        }

        out.write(sb.toString());
        out.flush();
    }

    /*private void renderMultiplexer(Canvas canvas) {
        //todo
        if (canvas == null)
            throw new RuntimeException("Canvas is null on rendering!");

        char[][] matrix = canvas.getMatrix();
        Ansi[][] colors = canvas.getColors();
        int outRealHeight = matrix.length;// * screen.getMultiplexer();
        int outRealWidth  = matrix[0].length;// * canvas.getMultiplexer();
        StringBuilder sb = new StringBuilder(outRealHeight * outRealWidth + height - outRealHeight + width - outRealWidth);
        StringBuilder lineSb = new StringBuilder(outRealWidth + 1);

        sb.append(S_CDL);
        if (CongasCore.isDebug())
            sb.append(S_NL);

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

                //for (int mc = 0; mc < canvas.getMultiplexer(); mc++)
                    lineSb.append(c);

            }
            lineSb.append(S_NL);
            //for (int mc = 0; mc < canvas.getMultiplexer(); mc++)
                sb.append(lineSb);
        }

        sb.append(S_RST);
        for (int i = 0; i < (height - outRealHeight); i++)
            sb.append(S_NL);

        if (CongasCore.isDebug()) {
            sb.append("FPS: ").append(averageFps).append('/').append(screen.getFps())
                    .append(" (").append(minFps).append(" - ").append(maxFps).append(')');
        }
        out.write(sb.toString());
        out.flush();
    }
    */

    /**
     * Called when terminal resize detected
     * @param w new width
     * @param h new height
     */
    private void resize(int w, int h) {
        if (width != w || height != h) {
            if (CongasCore.isDebug())
                logger.info("Terminal resized from " + width + "x" + height + " to " + w + "x" + h);
            width = w;
            height = h;
        }
        if (screen != null)
            screen.updateSize(this, width, height);
    }

    public void setScreen(ScreenBuffer screen) {
        this.screen = screen;
        updateScreenSize = true;
    }

    public static int getTerminalWidth() {
        return width;
    }

    public static int getTerminalHeight() {
        return height;
    }

}
