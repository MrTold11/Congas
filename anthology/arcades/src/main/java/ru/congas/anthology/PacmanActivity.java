package ru.congas.anthology;

import ru.congas.core.application.Bundle;
import ru.congas.core.application.GameActivity;
import ru.congas.core.input.keys.KeyPressed;

public class PacmanActivity extends GameActivity {

    Pacmen pacmen = new Pacmen();
    Red red = new Red();
    Settings settings = new Settings();
    Field field = new Field();

    char[][] f;

    public PacmanActivity() {
        field.read();
        setTps(5);
    }

    @Override
    public void onCreate(Bundle args) {
        super.onCreate(args);
        this.screen.getCanvas().clear(33, 35);
    }

    @Override
    public void onMainLoop() {
        runOnUiThread(() -> {
            f = settings.newOutput(pacmen, red, field.field(), getCanvas());
            red.move(pacmen.getX(), pacmen.getY(), f, settings);
            pacmen.move(f);
            screen.updateCanvas();
        });
    }

    @Override
    public boolean handle(KeyPressed event) {
        return false;
    }

    public void onResize(int oldWidth, int oldHeight, int newWidth, int newHeight) {

    }
}