package ru.congas.output;

import org.jline.terminal.Terminal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.fusesource.jansi.Ansi;
import ru.congas.CongasClient;

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

    private int width, height;

    public RenderThread() {
        super("Renderer");
        this.terminal = CongasClient.terminal;
        this.out = terminal.output();
        width = terminal.getWidth();
        height = terminal.getHeight();
    }

    /**
     * Thread main loop. Register terminal resize, call render, count fps
     */
    public void run() {
        int nw, nh;         // new width and height
        int whc = 0;        // width-height time counter, used for check width and height every 500ms only
        try {
            long loopTimer; // timer for loop time (to keep target fps)
            while (CongasClient.run) {
                loopTimer = System.currentTimeMillis();

                if (whc > 500) {
                    nw = terminal.getWidth();
                    nh = terminal.getHeight();
                    if (nw != width || nh != height) resize(nw, nh);
                }

                if (canvas.liveUpdate() || canvas.updateNeeded()) {
                    render();
                    canvas.forceUpdate(false);
                }

                loopTimer = System.currentTimeMillis() - loopTimer;
                if (loopTimer < canvas.getLoopTime()) //noinspection BusyWait
                    sleep(canvas.getLoopTime() - loopTimer);
                whc += canvas.getLoopTime();
            }
        } catch (Exception e) {
            logger.fatal(e);
            System.err.println("Something bad has happened to renderer, sorry");
        }
    }

    /**
     * Render frame with properties from canvas
     * @throws IOException if terminal print goes wrong
     */
    private void render() throws IOException {
        if (canvas == null) {
            logger.error("Canvas is null on rendering!");
            out.write((Ansi.ansi().bg(Ansi.Color.RED).toString() + "No canvas....." + Ansi.ansi().reset().toString()).getBytes());
            //todo go to main menu?
            return;
        }

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
        out.write(sb.toString().getBytes());
    }

    /**
     * Called when terminal resize detected
     * @param w new width
     * @param h new height
     */
    private void resize(int w, int h) {
        if (CongasClient.debug) logger.info("Terminal resized from " + width + "x" + height + " to " + w + "x" + h);
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
        if (CongasClient.debug) logger.info("Canvas set to " + c.getName());
        this.canvas = c;
        canvas.updateTerminal(width, height);
    }

}
