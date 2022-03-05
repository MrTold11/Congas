package ru.congas.pages;

import ru.congas.core.application.Bundle;
import ru.congas.core.audio.AudioManager;
import ru.congas.core.output.modifier.Color;
import ru.congas.core.output.modifier.Style;
import ru.congas.loader.StorageManager;

/**
 * @author Mr_Told
 */
public final class MainMenu extends AbstractValueSelector {

    @Override
    public void onCreate(Bundle args) {
        super.onCreate(generate("Main menu",
                new Style(Color.FOREST), new Style(Color.BLUE), new Style(Color.PURPLE),
                true,
                "Library", "Store", "Settings"));
    }

    @Override
    protected void selected(String value) {
        switch (value) {
            case "Library":
                if (StorageManager.hasApps())
                    openActivity(LibrarySelector.class, null, false);
                else
                    logger.info("No apps loaded");
                break;
            case "Store":
                AudioManager.playClip(getClass().getResource("/audio/test.wav"));
                break;
            case "Settings":
                openActivity(SettingsPage.class, null, false);
                break;
        }
    }

}
