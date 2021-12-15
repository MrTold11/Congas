package ru.congas.pages;

import ru.congas.CongasClient;

/**
 * @author Mr_Told
 */
public final class CloseCongas extends AbstractDialog {

    public CloseCongas() {
        super("No", "Yes", null, true, "Exit", "", "Close Congas?");
    }

    @Override
    protected void clickA() {
        CongasClient.back();
    }

    @Override
    protected void clickB() {
        CongasClient.close();
    }

}
