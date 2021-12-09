package ru.congas.input;

import ru.congas.CongasClient;

/**
 * @author Mr_Told
 */
public class SystemHandler implements InputHandler {

    @Override
    public boolean handle(int c) {
        if (c == Keycode.ESCAPE) {
            CongasClient.close();
            return true;
        }
        return false;
    }

}
