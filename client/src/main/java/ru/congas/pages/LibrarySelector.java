package ru.congas.pages;

import ru.congas.CongasClient;
import ru.congas.loader.AnthologyLoader;
import ru.congas.loader.StorageManager;

/**
 * @author Mr_Told
 */
public final class LibrarySelector extends AbstractValueSelector {

    public LibrarySelector() {
        super("Library", "Anthologies library", true, false, StorageManager.getLoadedAnthologies());
    }

    @Override
    protected void selected(String value) {
        AnthologyLoader al = StorageManager.getLoader(value);
        if (al.appsCount() > 1) CongasClient.openPage(new AppSelector(StorageManager.getLoader(value)));
        else CongasClient.openPage(al.getNewAppInstance(al.getApps()[0]));
    }

}
