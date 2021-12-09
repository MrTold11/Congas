package ru.congas.output;

import org.fusesource.jansi.Ansi;
import ru.congas.CongasClient;

/**
 * @author Mr_Told
 */
public abstract class Canvas {

    final protected RenderThread renderer = CongasClient.renderer;

    public Canvas() {
        init();
    }

    /**
     * The 'game field'. Should be as small as possible (otherwise it's part won't be visible)
     */
    private char[][] matrix;
    private Ansi[][] colors;

    /**
     * Void for initializing width and height
     */
    protected abstract void init();

    protected void initCanvas(int w, int h) {
        matrix = new char[h][w];
        colors = new Ansi[h][w];
    }

    /**
     * Optional event that calls on every terminal resize
     * @param w width
     * @param h height
     */
    public void resized(int w, int h) {

    }

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
}
