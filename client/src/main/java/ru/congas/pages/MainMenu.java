package ru.congas.pages;

import ru.congas.CongasClient;

/**
 * @author Mr_Told
 */
public class MainMenu extends MenuSelector {

    public MainMenu() {
        super("MainMenu", "Anthologies library", true, CongasClient.storageManager.getLoadedAnthologies());
    }

    @Override
    protected void selected(String value) {
        exit(new GameSelector(CongasClient.storageManager.getLoader(value)));
    }

}
