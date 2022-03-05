package ru.congas.core.output.widgets;

import ru.congas.core.output.canvas.Canvas;
import ru.congas.core.output.modifier.Style;

/**
 * @author Mr_Told
 */
public class TextView extends Widget {

    String string;
    Style style;

    public TextView(String text, Style style) {
        string = text;
        this.style = style;
    }

    public String getText() {
        return string;
    }

    public void setText(String string) {
        this.string = string;
    }

    public Style getPattern() {
        return style;
    }

    public void setPattern(Style colors) {
        this.style = colors;
    }

    @Override
    public void render(Canvas canvas) {
        int startX = position.getXCoordinate(string.length(), canvas.getWidth());
        int contentX = position.getContentX(string.length(), canvas.getWidth());
        int contentWidth = position.getContentWidth(string.length(), canvas.getWidth());
        int y = position.getYCoordinate(1, canvas.getHeight());
        int endX = position.getEndXCoordinate(startX, string.length(), canvas.getWidth());
        for (int i = startX; i < endX; i++)
            canvas.getCell(y, i).setStyle(background);

        for (int i = contentX; i < contentWidth + contentX; i++) {
            canvas.getCell(y, i)
                    .setChar(string.charAt(i - contentX))
                    .setStyle(style);
        }
    }

}
