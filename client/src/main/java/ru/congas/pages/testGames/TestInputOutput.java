package ru.congas.pages.testGames;

import org.fusesource.jansi.Ansi;
import ru.congas.SimpleGame;

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
    public boolean handle(int c) {
        if (c == 'w') {
            a++;
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
