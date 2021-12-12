package ru.congas.pages;

import ru.congas.CongasClient;

/**
 * @author Mr_Told
 */
public class LibrarySelector extends MenuSelector {

    public LibrarySelector() {
        super("Library", "Anthologies library", true, CongasClient.storageManager.getLoadedAnthologies());
    }

    @Override
    protected void selected(String value) {
        exit(new GameSelector(CongasClient.storageManager.getLoader(value)));
    }

}
