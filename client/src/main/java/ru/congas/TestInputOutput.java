package ru.congas;

import org.fusesource.jansi.Ansi;
import ru.congas.input.Keycode;

import java.util.Random;

/**
 * @author Mr_Told
 */
public class TestInputOutput extends SimpleGame {

    final Ansi star = Ansi.ansi().bgCyan();
    int a = 0;

    @Override
    public boolean handle(int c) {
        switch (c) {
            case Keycode.ESCAPE:
                CongasClient.close();
                return true;
            case 'w':
                a++;
                renderer.needUpdate();
                return true;
        }
        return false;
    }

    @Override
    protected void init() {
        initCanvas(237, 66);
        //renderer.enableMultiplexer(false);
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
