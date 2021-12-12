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
    final String name;

    private volatile boolean eraseScreen = true; //Clear console output before new frame render
    private volatile boolean resetMatrix = true; //Reset matrix[][] and colors[][] before update

    /**
     * Multiplexer evenly increases render size. Example: matrix is 10x10, but terminal size is
     * 120x40. In that case multiplexer will be 4 (max in every direction) so every char will be
     * printed 4 times, and every line will be printed 4 times too (output is simply 4 times bigger)
     */
    private volatile int multiplexer = 1;
    private boolean enableMultiplexer = false;

    private volatile boolean liveUpdate = false; //Update console on every frame
    private volatile boolean needUpdate = false; //Force console update

    //Frames per second. If zero, fps is not limited
    private int fps = 30;
    private volatile int loopTime = 1000 / fps; //ms

    private int terminalWidth, terminalHeight;

    /**
     * The 'game field'. Should be as small as possible (otherwise it's part won't be visible)
     */
    private volatile char[][] matrix = null;
    private volatile Ansi[][] colors;

    /**
     * Main canvas constructor
     * @param name game name (for logger)
     * @param enableMultiplexer enable multiplexer (ignored)
     * @param eraseScreen erase screen on every frame render
     * @param resetMatrix reset matrices before it's update
     * @param liveUpdate update console output on every frame
     * @param fps frames per second (if liveUpdate is enabled) and loop iterations per second
     * @param matrix_w initial matrices width
     * @param matrix_h initial matrices height
     */
    public Canvas(String name, boolean enableMultiplexer, boolean eraseScreen, boolean resetMatrix,
                  boolean liveUpdate, int fps, int matrix_w, int matrix_h) {
        logger = LogManager.getLogger("Canvas_" + name);
        this.name = name;
        initCanvas(matrix_w, matrix_h);
        //enableMultiplexer(enableMultiplexer);
        setLiveUpdate(liveUpdate);
        setFps(fps);
        setEraseScreen(eraseScreen);
        setResetMatrix(resetMatrix);
    }

    /**
     * Init matrices
     * @param w matrices width
     * @param h matrices height
     */
    protected void initCanvas(int w, int h) {
        if (w <= 0) w = 1;
        if (h <= 0) h = 1;
        if (matrix != null && w == matrix[0].length && h == matrix.length) return;
        if (CongasClient.debug) logger.info("Canvas set to " + w + "x" + h);
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

    public int getMatrixWidth() {
        return matrix[0].length;
    }

    public int getMatrixHeight() {
        return matrix.length;
    }

    public final int getMultiplexer() {
        return multiplexer;
    }

    /**
     * Calls on terminal size update
     * @param w new width
     * @param h new height
     */
    public final void updateTerminal(int w, int h) {
        terminalWidth = w;
        terminalHeight = h;
        resized(w, h);
        resetMultiplexer();
        forceUpdate();
    }

    /**
     * Clear existing matrices
     */
    protected final void resetMatrices() {
        if (matrix == null || colors == null) return;

        int l1 = matrix.length;
        int l2 = matrix[0].length;
        matrix = new char[l1][l2];
        colors = new Ansi[l1][l2];
    }

    /**
     * @param enable enable multiplexer
     * @deprecated as it looks bad overall and especially with text. Should not be used without strong need
     */
    public final void enableMultiplexer(boolean enable) {
        enableMultiplexer = enable;
        resetMultiplexer();
    }

    /**
     * Compute multiplexer value if enabled
     */
    public final void resetMultiplexer() {
        multiplexer = 0;
        if (enableMultiplexer)
            multiplexer = Math.min(terminalHeight / matrix.length, terminalWidth / matrix[0].length);
        if (multiplexer <= 0) multiplexer = 1;
        if (CongasClient.debug) logger.info("Multiplexer set to " + multiplexer);
        forceUpdate();
    }

    /**
     * Set output fps and how often render loop processed
     * @param fps frames per second
     */
    protected final void setFps(int fps) {
        if (CongasClient.debug) logger.info("Fps set from " + getFps() + " to " + fps);
        this.fps = fps;
        loopTime = fps <= 0 ? 0 : 1000 / fps;
    }

    protected void setLiveUpdate(boolean liveUpdate) {
        if (CongasClient.debug) logger.info("Live update is " + (liveUpdate ? "on" : "off"));
        this.liveUpdate = liveUpdate;
    }

    public void forceUpdate(boolean needUpdate) {
        this.needUpdate = needUpdate;
    }

    public void forceUpdate() {
        forceUpdate(true);
    }

    protected void setEraseScreen(boolean eraseScreen) {
        if (CongasClient.debug) logger.info("Screen erase is " + (eraseScreen ? "on" : "off"));
        this.eraseScreen = eraseScreen;
    }

    protected void setResetMatrix(boolean resetMatrix) {
        if (CongasClient.debug) logger.info("Matrices reset is " + (resetMatrix ? "on" : "off"));
        this.resetMatrix = resetMatrix;
    }

    public int getFps() {
        return fps;
    }

    public int getLoopTime() {
        return loopTime;
    }

    public boolean liveUpdate() {
        return liveUpdate;
    }

    public boolean updateNeeded() {
        return needUpdate;
    }

    public boolean eraseScreen() {
        return eraseScreen;
    }

    public boolean resetMatrix() {
        return resetMatrix;
    }

    public String getName() {
        return name;
    }

}
