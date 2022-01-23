package ru.congas.input;

import ru.congas.CongasClient;
import ru.congas.SimpleApp;
import ru.congas.input.keys.Key;
import ru.congas.input.keys.KeyPressed;
import ru.congas.pages.AppPause;

/**
 * @author Mr_Told
 */
public class SystemHandler implements InputHandler {

    private long escTime = 0;
    private volatile SimpleApp current = null;

    @Override
    public boolean handle(KeyPressed event) {
        if (current == null) return false;
        if (event.getDefinedKey() == Key.ESCAPE) {
            if (System.currentTimeMillis() - escTime < 250) {
                if (current.overrideEscape()) CongasClient.back();
                else CongasClient.close();
                return true;
            }
            escTime = System.currentTimeMillis();
            if (current.overrideEscape()) return false;
            if (current.isGame())
                CongasClient.openPage(new AppPause());
            else
                CongasClient.back();
            return true;
        }
        return false;
    }

    @Override
    public String getHandlerName() {
        return "SystemHandler";
    }

    public void setCurrent(SimpleApp app) {
        this.current = app;
    }

}
