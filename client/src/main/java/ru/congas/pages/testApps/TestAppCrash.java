package ru.congas.pages.testApps;

import ru.congas.SimpleGame;
import ru.congas.input.Keycode;
import ru.congas.output.widgets.TextView;

/**
 * @author Mr_Told
 */
public class TestAppCrash extends SimpleGame {

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
    public boolean handle(int c) {
        if (c == Keycode.SPACE) crashOnRender = true;
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