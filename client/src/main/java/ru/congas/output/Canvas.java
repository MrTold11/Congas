package ru.congas.output;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.fusesource.jansi.Ansi;
import ru.congas.CongasClient;

/**
 * @author Mr_Told
 */
public abstract class Canvas {

    final protected Logger logger;

    private int multiplexer = 1;
    private boolean enableMultiplexer = true;

    private volatile boolean liveUpdate = false; //Update console on every frame
    private volatile boolean needUpdate = false; //Force console update

    //Frames per second. If zero, fps is not limited
    private int fps = 30;
    private volatile int loopTime = 1000 / fps; //ms

    private int terminalWidth, terminalHeight;

    public Canvas(String name, boolean enableMultiplexer, boolean liveUpdate, int fps, int matrix_w, int matrix_h) {
        logger = LogManager.getLogger("Canvas_" + name);
        initCanvas(matrix_w, matrix_h);
        enableMultiplexer(enableMultiplexer);
        setLiveUpdate(liveUpdate);
        setFps(fps);
    }

    /**
     * The 'game field'. Should be as small as possible (otherwise it's part won't be visible)
     */
    private char[][] matrix;
    private Ansi[][] colors;

    protected void initCanvas(int w, int h) {
        matrix = new char[h][w];
        colors = new Ansi[h][w];
    }

    /**
     * Optional event that calls on every terminal resize
     * @param w width
     * @param h height
     */
    protected void resized(int w, int h) {}

    /**
     * Put all visible elements on canvas matrix
     */
    public abstract void updateCanvas();

    public char[][] getMatrix() {
        return matrix;
    }

    public Ansi[][] getColors() {
        return colors;
    }

    public final int getMultiplexer() {
        return multiplexer;
    }

    public final void updateTerminal(int w, int h) {
        terminalWidth = w;
        terminalHeight = h;
        resized(w, h);
        resetMultiplexer();
    }

    public final void enableMultiplexer(boolean enable) {
        enableMultiplexer = enable;
        resetMultiplexer();
    }

    public final void resetMultiplexer() {
        multiplexer = 0;
        if (enableMultiplexer)
            multiplexer = Math.min(terminalHeight / matrix.length, terminalWidth / matrix[0].length);
        if (multiplexer <= 0) multiplexer = 1;
        if (CongasClient.debug) logger.info("Multiplexer set to " + multiplexer);
    }

    protected void setFps(int fps) {
        if (CongasClient.debug) logger.info("Fps set from " + getFps() + " to " + fps);
        this.fps = fps;
        loopTime = fps <= 0 ? 0 : 1000 / fps;
    }

    public int getFps() {
        return fps;
    }

    public int getLoopTime() {
        return loopTime;
    }

    protected void setLiveUpdate(boolean liveUpdate) {
        if (CongasClient.debug) logger.info("Live update is " + (liveUpdate ? "on" : "off"));
        this.liveUpdate = liveUpdate;
    }

    public void needUpdate() {
        needUpdate = true;
    }

    public void notNeedUpdate() {
        needUpdate = false;
    }

    public boolean liveUpdate() {
        return liveUpdate;
    }

    public boolean updateNeeded() {
        return needUpdate;
    }

}
