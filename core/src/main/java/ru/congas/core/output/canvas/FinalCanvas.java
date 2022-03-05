package ru.congas.core.output.canvas;

import org.apache.logging.log4j.LogManager;

/**
 * @author Mr_Told
 */
public class FinalCanvas {

    private final static String S_NL = System.getProperty("line.separator");

    private final Frame frame = new Frame();
    Canvas target;

    public FinalCanvas(Canvas target) {
        this.target = target;
    }

    public void build() {
        Cell[][] matrix = target.getMatrix();
        StringBuilder sb = new StringBuilder(matrix.length * matrix[0].length);

        Cell current = null;
        for (Cell[] cells : matrix) {
            for (int ch = 0; ch < matrix[0].length; ch++)
                current = cells[ch].render(current, sb);
            sb.append(S_NL);
        }

        frame.set(sb.toString(), matrix.length, matrix[0].length);
    }

    public Frame getFrame() {
        return frame;
    }

}
