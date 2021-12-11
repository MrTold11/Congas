package ru.congas;

import ru.congas.input.InputHandler;
import ru.congas.output.Canvas;

/**
 * @author Mr_Told
 */
public abstract class SimpleGame extends Canvas implements InputHandler {

    private final String name;

    public SimpleGame(String name, boolean enableMultiplexer, boolean liveUpdate, int fps, int matrix_w, int matrix_h) {
        super(name, enableMultiplexer, liveUpdate, fps, matrix_w, matrix_h);
        this.name = name;
    }

    public void launch() {
        CongasClient.renderer.setCanvas(this);
        CongasClient.input.addHandler(this);
    }

    public String getName() {
        return name;
    }

}
