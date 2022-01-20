package ru.congas.output.widgets;

import org.fusesource.jansi.Ansi;
import ru.congas.output.Canvas;

/**
 * @author Mr_Told
 */
public class TextView extends Widget {

    String string;
    Ansi colors;

    public TextView(String text, Ansi colors) {
        string = text;
        this.colors = colors;
    }

    public String getText() {
        return string;
    }

    public void setText(String string) {
        this.string = string;
    }

    public Ansi getColors() {
        return colors;
    }

    public void setColors(Ansi colors) {
        this.colors = colors;
    }

    @Override
    public void render(Canvas canvas) {
        int startX = position.getXCoordinate(string.length(), canvas.getMatrixWidth());
        int contentX = position.getContentX(string.length(), canvas.getMatrixWidth());
        int contentWidth = position.getContentWidth(string.length(), canvas.getMatrixWidth());
        int y = position.getYCoordinate(1, canvas.getMatrixHeight());
        int endX = position.getEndXCoordinate(startX, string.length(), canvas.getMatrixWidth());
        for (int i = startX; i < endX; i++) {
            //canvas.getMatrix()[y][i] = string.charAt(i - startX);
            canvas.getColors()[y][i] = backgroundColor;
        }
        for (int i = contentX; i < contentWidth + contentX; i++) {
            canvas.getMatrix()[y][i] = string.charAt(i - contentX);
            canvas.getColors()[y][i] = colors;
        }
    }

}
