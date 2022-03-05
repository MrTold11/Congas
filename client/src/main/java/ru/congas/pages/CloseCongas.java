package ru.congas.pages;

import ru.congas.CongasClient;
import ru.congas.core.application.Bundle;
import ru.congas.core.output.modifier.Color;
import ru.congas.core.output.modifier.Style;
import ru.congas.core.pages.AbstractDialog;

/**
 * @author Mr_Told
 */
public final class CloseCongas extends AbstractDialog {

    boolean opened = false;

    @Override
    public void onCreate(Bundle args) {
        super.onCreate(generate("No", "Yes", null,
                new Style(Color.CYAN),
                new Style(Color.fromRgb(205, 83, 52)),
                "Exit", "", "Close Congas?"));
    }

    @Override
    public void onResume(Bundle result) {
        super.onResume(result);
        if (!opened)
            runOnUiThread(this::clickA);
        opened = true;
    }

    @Override
    protected void clickA() {
        openActivity(MainMenu.class, null, false);
    }

    @Override
    protected void clickB() {
        CongasClient.close(this);
    }

}
