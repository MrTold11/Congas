package ru.congas.pages.testApps;

import ru.congas.core.application.Bundle;
import ru.congas.core.application.GameActivity;
import ru.congas.core.input.keys.Key;
import ru.congas.core.input.keys.KeyPressed;
import ru.congas.core.output.modifier.Color;
import ru.congas.core.output.modifier.Style;

import java.util.Random;

/**
 * @author Mr_Told
 */
@SuppressWarnings("unused")
public class TestInputOutput extends GameActivity {

    final Style star = new Style(Color.CYAN);
    int a = 0;

    @Override
    public void onCreate(Bundle args) {
        super.onCreate(args);
        setTps(30);
    }

    @Override
    public boolean handle(KeyPressed event) {
        if (event.getDefinedKey() == Key.UP) {
            a++;
            screen.updateCanvas();
            return true;
        } else if (event.getDefinedKey() == Key.DOWN) {
            a--;
            screen.updateCanvas();
            return true;
        }
        return false;
    }

    @Override
    public void onMainLoop() {
        Random r = new Random(System.currentTimeMillis());
        runOnUiThread(() -> {
            for (int i = 0; i < a; i++) {
                int x = r.nextInt(screen.getWidth());
                int y = r.nextInt(screen.getHeight());
                getCanvas().getCell(y, x)
                        .setChar(Math.random() > 0.5 ? '/' : '\\')
                        .setStyle(new Style(Color.fromRgb((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255))));
                        //.setStyle(star);
                //getColors()[y][x] = star;
            }
            screen.updateCanvas();
        });
    }
}
