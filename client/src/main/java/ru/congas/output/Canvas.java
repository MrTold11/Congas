package ru.congas.output;

import org.fusesource.jansi.Ansi;

/**
 * @author Mr_Told
 */
public abstract class Canvas {

    /**
     * The 'game field'. Should be as small as possible (otherwise it's part won't be visible)
     */
    private char[][] matrix;
    private Ansi[][] colors;

    private void initCanvas(int w, int h) {
        matrix = new char[h][w];
        colors = new Ansi[h][w];
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
