package ru.congas.anthology;

import ru.congas.core.application.Bundle;
import ru.congas.core.application.GameActivity;
import ru.congas.core.input.keys.KeyPressed;

public class PacmanActivity extends GameActivity {

    Pacmen pacmen = new Pacmen();
    Red red = new Red();
    Settings settings = new Settings();
    Field field = new Field();

    int score = 0;

    char[][] f;

    public PacmanActivity() {
        field.read();
        setTps(5);
    }

    @Override
    public void onCreate(Bundle args) {
        super.onCreate(args);
        //todo generate field
        this.screen.getCanvas().clear(29, 32);
    }

    @Override
    public void onMainLoop() {
        runOnUiThread(() -> {
            f = settings.newOutput(pacmen, red, field.field(), getCanvas());
            red.move(pacmen.getX(), pacmen.getY(), f, settings, this);
            pacmen.move(f);
            screen.updateCanvas();
        });
    }

    @Override
    public boolean handle(KeyPressed event) {
        switch (event.getDefinedKey()) {
            case LEFT:
                pacmen.setMx(-1);
                return true;
            case RIGHT:
                pacmen.setMx(1);
                return true;
            case UP:
                pacmen.setMy(-1);
                return true;
            case DOWN:
                pacmen.setMy(1);
                return true;
        }
        return false;
    }

    public void onResize(int oldWidth, int oldHeight, int newWidth, int newHeight) {

    }
}