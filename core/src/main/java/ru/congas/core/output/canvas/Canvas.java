package ru.congas.core.output.canvas;

import ru.congas.core.output.modifier.Style;

/**
 * @author Mr_Told
 */
public class Canvas {

    /**
     * The 'game field'. Should be as small as possible (otherwise it's part won't be visible)
     */
    private Cell[][] matrix;

    public Canvas(int matrix_w, int matrix_h) {
        clear(matrix_w, matrix_h);
    }

    public void copy(Canvas from) {
        this.matrix = from.matrix.clone();
    }

    public Cell[][] getMatrix() {
        return matrix;
    }

    public Cell getRawCell(int y, int x) {
        return matrix[y][x];
    }

    public Cell getCell(int y, int x) {
        if (matrix[y][x] == null)
            matrix[y][x] = new Cell();
        return getRawCell(y, x);
    }

    public int getWidth() {
        return matrix[0].length;
    }

    public int getHeight() {
        return matrix.length;
    }

    /**
     * Clear existing matrices
     */
    public void clear() {
        if (matrix == null) clear(1, 1);

        int l1 = matrix.length;
        int l2 = matrix[0].length;
        matrix = new Cell[l1][l2];
        fill(null, ' ');
    }

    public void clear(int w, int h) {
        if (w <= 0) w = 1;
        if (h <= 0) h = 1;

        matrix = new Cell[h][w];
        fill(null, ' ');
    }

    //todo add background cell
    public void fill(Style style, char c) {
        for (int i = 0; i < getHeight(); i++) {
            for (int j = 0; j < getWidth(); j++)
                getCell(i, j).setChar(c).setStyle(style);
        }
    }

}
