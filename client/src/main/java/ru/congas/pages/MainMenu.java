package ru.congas.pages;

import ru.congas.CongasClient;
import ru.congas.audio.AudioManager;
import ru.congas.loader.StorageManager;

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
                if (StorageManager.hasApps())
                    CongasClient.openPage(new LibrarySelector());
                else logger.info("No apps loaded");
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
