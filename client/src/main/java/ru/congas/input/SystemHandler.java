package ru.congas.input;

import ru.congas.CongasClient;
import ru.congas.SimpleGame;
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
            if (System.currentTimeMillis() - escTime < 300) {
                if (current.overrideEscape()) CongasClient.back();
                else CongasClient.close();
                return true;
            }
            escTime = System.currentTimeMillis();
            if (current.overrideEscape()) return false;
            //todo open pause if (current.isGame())
            CongasClient.back();
            return true;
        }
        return false;
    }

    @Override
    public String getHandlerName() {
        return "SystemHandler";
    }

    public void setCurrent(SimpleGame game) {
        this.current = game;
    }

}
