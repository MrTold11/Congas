package ru.congas;

import ru.congas.input.InputHandler;
import ru.congas.output.Canvas;
import ru.congas.pages.MainMenu;

/**
 * @author Mr_Told
 */
public abstract class SimpleGame extends Canvas implements InputHandler {

    public SimpleGame(String name, boolean enableMultiplexer, boolean eraseScreen,
                      boolean resetMatrix, boolean liveUpdate, int fps, int matrix_w, int matrix_h) {
        super(name, enableMultiplexer, eraseScreen, resetMatrix, liveUpdate, fps, matrix_w, matrix_h);
    }

    public void launch() {
        CongasClient.renderer.setCanvas(this);
        CongasClient.input.addHandler(this);
    }

    public String getHandlerName() {
        return getName() + "_handler";
    }

    protected final void exit() {
        CongasClient.input.removeHandler(this);
        new MainMenu().launch();
    }

    protected final void exit(SimpleGame page) {
        CongasClient.input.removeHandler(this);
        page.launch();
    }

}
