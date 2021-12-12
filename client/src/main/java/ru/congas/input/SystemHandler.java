package ru.congas.input;

import ru.congas.CongasClient;
import ru.congas.pages.MainMenu;

/**
 * @author Mr_Told
 */
public class SystemHandler implements InputHandler {

    private long escTime = 0;

    @Override
    public boolean handle(int c) {
        if (c == Keycode.ESCAPE) {
            if (System.currentTimeMillis() - escTime < 300 || CongasClient.renderer.getCanvasClass().equals(MainMenu.class))
                CongasClient.close();
            escTime = System.currentTimeMillis();

            return true;
        }
        return false;
    }

    @Override
    public String getHandlerName() {
        return "SystemHandler";
    }

}
