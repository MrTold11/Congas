package ru.congas.output.widgets;

import ru.congas.output.Canvas;

/**
 * @author Mr_Told
 */
public abstract class Widget {

    WidgetPosition position = new WidgetPosition();

    public abstract void render(Canvas canvas);

    public WidgetPosition setPos() {
        return position;
    }

}
