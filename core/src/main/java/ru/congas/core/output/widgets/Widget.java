package ru.congas.core.output.widgets;

import ru.congas.core.output.canvas.Canvas;
import ru.congas.core.output.modifier.Style;
import ru.congas.core.output.widgets.properties.WidgetPosition;

/**
 * @author Mr_Told
 */
public abstract class Widget {

    WidgetPosition position = new WidgetPosition();
    Style background;

    public abstract void render(Canvas canvas);

    public WidgetPosition pos() {
        return position;
    }

    public Style getBackground() {
        return background;
    }

    public void setBackgroundStyle(Style bg) {
        background = bg;
    }

}
