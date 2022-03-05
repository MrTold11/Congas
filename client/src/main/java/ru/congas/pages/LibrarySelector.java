package ru.congas.pages;

import ru.congas.core.application.Activity;
import ru.congas.core.application.Bundle;
import ru.congas.core.loader.AppsLoader;
import ru.congas.core.output.modifier.Color;
import ru.congas.core.output.modifier.Style;
import ru.congas.loader.StorageManager;

/**
 * @author Mr_Told
 */
public final class LibrarySelector extends AbstractValueSelector {

    @Override
    public void onCreate(Bundle args) {
        super.onCreate(generate("Apps library",
                new Style(Color.FOREST), new Style(Color.BLUE), new Style(Color.PURPLE),
                true,
                StorageManager.getLoadedAnthologies()));
    }

    @Override
    protected void selected(String value) {
        AppsLoader al = StorageManager.getLoader(value);
        if (al.appsCount() > 1)
            openActivity(AppSelector.class, new Bundle().addExtra("loader", al), false);
        else {
            Class<? extends Activity> appClass = al.getClass(al.getApps()[0]);
            Bundle bundle = null;
            if (appClass == null) {
                appClass = AppNotFound.class;
                bundle = new Bundle().addExtra("name", al.getApps()[0]).addExtra("package", al.getName());
            }
            openActivity(appClass, bundle, false);
        }
    }

}
