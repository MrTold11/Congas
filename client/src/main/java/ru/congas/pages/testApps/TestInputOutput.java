package ru.congas.pages.testApps;

import org.fusesource.jansi.Ansi;
import ru.congas.SimpleGame;
import ru.congas.input.keys.Key;
import ru.congas.input.keys.KeyPressed;

import java.util.Random;

/**
 * @author Mr_Told
 */
public class TestInputOutput extends SimpleGame {

    final Ansi star = Ansi.ansi().bgCyan();
    int a = 0;

    public TestInputOutput() {
        super("TestIO", false, true, 30, 10, 10);
    }

    @Override
    public boolean handle(KeyPressed event) {
        if (event.getDefinedKey() == Key.KEY_W || event.getDefinedKey() == Key.UP) {
            a++;
            forceUpdate();
            return true;
        } else if (event.getDefinedKey() == Key.KEY_S || event.getDefinedKey() == Key.DOWN) {
            a--;
            forceUpdate();
            return true;
        }
        return false;
    }

    public void resized(int w, int h) {
        initCanvas(w, h);
    }

    @Override
    public void updateCanvas() {
        Random r = new Random(System.currentTimeMillis());
        for (int i = 0; i < a; i++) {
            int x = r.nextInt(getMatrix()[0].length);
            int y = r.nextInt(getMatrix().length);
            getColors()[y][x] = star;
        }
    }

}
