package ru.congas;

import ru.congas.input.InputHandler;
import ru.congas.output.Canvas;

/**
 * @author Mr_Told
 */
public abstract class SimpleGame extends Canvas implements InputHandler {

    public void launch() {
        CongasClient.renderer.setCanvas(this);
        CongasClient.input.addHandler(this);
    }

}
