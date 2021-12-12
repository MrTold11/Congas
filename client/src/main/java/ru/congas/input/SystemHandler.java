package ru.congas.input;

import ru.congas.CongasClient;
import ru.congas.pages.GameNotFound;
import ru.congas.pages.GameSelector;
import ru.congas.pages.LibrarySelector;
import ru.congas.pages.MainMenu;

/**
 * @author Mr_Told
 */
public class SystemHandler implements InputHandler {

    private long escTime = 0;

    @Override
    public boolean handle(int c) {
        if (c == Keycode.ESCAPE) {
            if (System.currentTimeMillis() - escTime < 300 || CongasClient.renderer.getCanvas() instanceof MainMenu)
                CongasClient.close();
            escTime = System.currentTimeMillis();
            if (CongasClient.renderer.getCanvas() instanceof LibrarySelector) {
                CongasClient.input.removeHandler((InputHandler) CongasClient.renderer.getCanvas());
                new MainMenu().launch();
            } else if (CongasClient.renderer.getCanvas() instanceof GameSelector || CongasClient.renderer.getCanvas() instanceof GameNotFound) {
                CongasClient.input.removeHandler((InputHandler) CongasClient.renderer.getCanvas());
                new LibrarySelector().launch();
            }
            return true;
        }
        return false;
    }

    @Override
    public String getHandlerName() {
        return "SystemHandler";
    }

}
