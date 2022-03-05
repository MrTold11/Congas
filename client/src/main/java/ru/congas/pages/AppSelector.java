package ru.congas.pages;

import ru.congas.core.application.Activity;
import ru.congas.core.application.Bundle;
import ru.congas.core.loader.AppsLoader;
import ru.congas.core.output.modifier.Color;
import ru.congas.core.output.modifier.Style;

/**
 * @author Mr_Told
 */
public final class AppSelector extends AbstractValueSelector {

    AppsLoader loader;

    @Override
    public void onCreate(Bundle args) {
        try {
            loader = (AppsLoader) args.getUnsafeObject("loader");
        } catch (Exception e) {
            throw new IllegalArgumentException("Dialog cannot be created without arguments", e);
        }
        super.onCreate(generate("Select app from " + loader.getName(),
                new Style(Color.FOREST), new Style(Color.BLUE), new Style(Color.PURPLE),
                true,
                loader.getApps()));
    }

    @Override
    protected void selected(String value) {
        Class<? extends Activity> appClass = loader.getClass(value);
        Bundle bundle = null;
        if (appClass == null) {
            appClass = AppNotFound.class;
            bundle = new Bundle().addExtra("name", value).addExtra("package", loader.getName());
        }
        openActivity(appClass, bundle, true);
    }

}
