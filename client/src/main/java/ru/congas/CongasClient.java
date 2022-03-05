package ru.congas;

import ru.congas.core.CongasCore;
import ru.congas.core.input.InputHandler;
import ru.congas.core.input.keys.Key;
import ru.congas.core.input.keys.KeyPressed;
import ru.congas.pages.CloseCongas;

/**
 * @author Mr_Told
 */
public class CongasClient extends CongasCore implements InputHandler {

    private long escTime = 0;

    public CongasClient() throws Exception {
        super();
        setTitle("Congas Client");
        openActivity(CloseCongas.class, null, this);
    }

    @Override
    public boolean handle(KeyPressed event) {
        if (current == null) return false;
        if (event.getDefinedKey() == Key.ESCAPE) {
            if (System.currentTimeMillis() - escTime < 250) {
                current.runOnUiThread(() -> current.closeActivity());
                return true;
            }
            escTime = System.currentTimeMillis();
        }
        return false;
    }

    @Override
    public String getHandlerName() {
        return "SystemHandler";
    }
}
