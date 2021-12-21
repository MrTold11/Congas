package ru.congas.pages;

import ru.congas.CongasClient;

/**
 * @author Mr_Told
 */
public final class AppPause extends AbstractValueSelector {

    public AppPause() {
        super("AppPause", "PAUSE", false, false, "Resume", "Settings", "Close app");
    }

    @Override
    protected void selected(String value) {
        switch (value) {
            case "Resume":
                CongasClient.back();
                break;
            case "Settings":
                CongasClient.openPage(new SettingsPage());
                break;
            case "Close app":
                CongasClient.openPage(new LibrarySelector());
                break;
        }
    }
}
