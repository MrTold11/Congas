package ru.congas.pages;

import ru.congas.loader.StorageManager;

/**
 * @author Mr_Told
 */
public class LibrarySelector extends AbstractValueSelector {

    public LibrarySelector() {
        super("Library", "Anthologies library", true, false, StorageManager.getLoadedAnthologies());
    }

    @Override
    protected void selected(String value) {
        exit(new GameSelector(StorageManager.getLoader(value)));
    }

}
