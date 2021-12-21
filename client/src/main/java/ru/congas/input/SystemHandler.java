package ru.congas.input;

import ru.congas.CongasClient;
import ru.congas.SimpleGame;
import ru.congas.pages.AppPause;

/**
 * @author Mr_Told
 */
public class SystemHandler implements InputHandler {

    private long escTime = 0;
    private volatile SimpleGame current = null;

    @Override
    public boolean handle(int c) {
        if (current == null) return false;
        if (c == Keycode.ESCAPE) {
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

    public void setCurrent(SimpleGame app) {
        this.current = app;
    }

}
