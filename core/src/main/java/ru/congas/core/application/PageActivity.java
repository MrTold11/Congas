package ru.congas.core.application;

import ru.congas.core.annotation.callsuper.CallSuper;
import ru.congas.core.output.widgets.Widget;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Mr_Told
 */
public abstract class PageActivity extends Activity {

    //todo add styles
    private final Set<Widget> widgets = Collections.synchronizedSet(new HashSet<>());

    protected Widget addWidget(@Nonnull Widget widget) {
        widgets.add(widget);
        return widget;
    }

    @CallSuper
    protected void render() {
        for (Widget w : widgets)
            w.render(getCanvas());
        screen.updateCanvas();
    }

    @Override
    public void onResize(int oldWidth, int oldHeight, int newWidth, int newHeight) {
        screen.getCanvas().clear(newWidth, newHeight);
        render();
        screen.updateCanvas();
    }

}
