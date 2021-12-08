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

    final static String RST = Ansi.ansi().reset().toString();

    final Logger logger = LogManager.getLogger(RenderThread.class);
    final Terminal terminal;
    OutputStream out;

    private volatile Canvas canvas = null;

    int fps = 30;
    volatile int loopTime = 1000 / fps; //ms

    private volatile boolean liveUpdate = false; //Update console on every frame
    private volatile boolean needUpdate = false; //Force console update

    private int width, height;

    public RenderThread() {
        super("Renderer");
        this.terminal = CongasClient.terminal;
        this.out = terminal.output();
    }

    public void run() {
        int nw, nh;
        try {
            long loopTimer;
            while (CongasClient.run) {
                loopTimer = System.currentTimeMillis();
                nw = terminal.getWidth();
                nh = terminal.getHeight();
                if (nw != width || nh != height) resize(nw, nh);

                if (liveUpdate || needUpdate) {
                    render();
                    needUpdate = false;
                }
                loopTimer = System.currentTimeMillis() - loopTimer;
                if (loopTimer < loopTime) //noinspection BusyWait
                    sleep(loopTime - loopTimer);
            }
        } catch (Exception e) {
            logger.fatal(e);
        }
    }

    private void render() throws IOException {
        if (canvas == null) {
            out.write((Ansi.ansi().bg(Ansi.Color.RED).toString() + "No canvas....." + Ansi.ansi().reset().toString()).getBytes());
            //todo go to main menu?
            return;
        }

        canvas.updateCanvas();
        //terminal.output().write((Ansi.ansi().bg(Ansi.Color.RED).toString() + (terminal.getWidth())).getBytes());
        //terminal.output().write(Ansi.ansi().reset().toString().getBytes());
        //terminal.output().write(Integer.toString(terminal.getHeight()).getBytes());
        //todo canvas render
    }

    private void resize(int w, int h) {
        width = w;
        height = h;
        needUpdate = true;
    }

    public void setCanvas(Canvas c) {
        this.canvas = c;
    }

    public void setFps(int fps) {
        this.fps = fps;
        if (fps == 0) loopTime = 0;
        else loopTime = 1000 / fps;
    }

    public int getFps() {
        return fps;
    }

    public void setLiveUpdate(boolean liveUpdate) {
        this.liveUpdate = liveUpdate;
    }

    public void needUpdate() {
        needUpdate = true;
    }

}
