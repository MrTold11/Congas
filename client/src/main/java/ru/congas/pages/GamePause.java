package ru.congas.pages;

import ru.congas.CongasClient;

/**
 * @author Mr_Told
 */
public final class GamePause extends AbstractValueSelector {

    public GamePause() {
        super("GamePause", "PAUSE", false, false, "Resume", "Settings", "Leave game");
    }

    @Override
    protected void selected(String value) {
        switch (value) {
            case "Resume":
                CongasClient.back();
                break;
            case "Settings":
                CongasClient.openPage(new SettingsPage());
                break;
            case "Leave game":
                CongasClient.openPage(new LibrarySelector());
                break;
        }
    }
}
