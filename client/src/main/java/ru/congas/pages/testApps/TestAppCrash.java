package ru.congas.pages.testApps;

import ru.congas.SimpleApp;
import ru.congas.input.keys.Key;
import ru.congas.input.keys.KeyPressed;
import ru.congas.output.widgets.TextView;

/**
 * @author Mr_Told
 */
@SuppressWarnings("unused")
public class TestAppCrash extends SimpleApp {

    TextView text = new TextView("Press [Space] for Render crash. Press any other key for Input crash.", null);
    int[] k;
    boolean crashOnRender = false;

    public TestAppCrash() {
        super("TestAppCrash", false, false, 10, 68, 10);
        setOverrideEscape(true);
    }

    public void resized(int w, int h) {
        initCanvas(w, h);
    }

    @Override
    public boolean handle(KeyPressed event) {
        if (event.getDefinedKey() == Key.SPACE) crashOnRender = true;
        else k[0] = 1;
        forceUpdate();
        return true;
    }

    @Override
    public void updateCanvas() {
        if (crashOnRender) k[0] = 1;
        text.render(this);
    }
}
