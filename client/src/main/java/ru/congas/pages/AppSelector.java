package ru.congas.pages;

import ru.congas.CongasClient;
import ru.congas.loader.AnthologyLoader;

/**
 * @author Mr_Told
 */
public final class AppSelector extends AbstractValueSelector {

    final AnthologyLoader loader;

    public AppSelector(AnthologyLoader loader) {
        super(loader.getName(), "Select app from " + loader.getName(), false, true, loader.getApps());
        this.loader = loader;
    }

    @Override
    protected void selected(String value) {
        CongasClient.openPage(loader.getNewAppInstance(value));
    }

}
