package ru.congas.pages;

import ru.congas.CongasClient;
import ru.congas.audio.AudioManager;

/**
 * @author Mr_Told
 */
public final class MainMenu extends AbstractValueSelector {

    public MainMenu() {
        super("MainMenu", "Congas Client", true, false, "Library", "Store", "Settings");
    }

    @Override
    protected void selected(String value) {
        switch (value) {
            case "Library":
                CongasClient.openPage(new LibrarySelector());
                break;
            case "Store":
                AudioManager.playClip(getClass().getResource("/audio/test.wav"));
                break;
            case "Settings":
                CongasClient.openPage(new SettingsPage());
                break;
        }
    }

}
