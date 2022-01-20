package ru.congas.output.widgets;

import org.fusesource.jansi.Ansi;
import ru.congas.output.Canvas;
import ru.congas.output.widgets.properties.WidgetPosition;

/**
 * @author Mr_Told
 */
public abstract class Widget {

    WidgetPosition position = new WidgetPosition();
    Ansi backgroundColor = null;

    public abstract void render(Canvas canvas);

    public WidgetPosition pos() {
        return position;
    }

    public Ansi getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Ansi bg) {
        backgroundColor = bg;
    }

}
