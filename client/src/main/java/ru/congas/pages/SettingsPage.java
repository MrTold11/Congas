package ru.congas.pages;

import ru.congas.CongasClient;
import ru.congas.core.CongasCore;
import ru.congas.core.application.Bundle;
import ru.congas.core.output.modifier.Color;
import ru.congas.core.output.modifier.Style;
import ru.congas.core.output.widgets.properties.Gravity;
import ru.congas.core.output.widgets.TextView;

/**
 * @author Mr_Told
 */
public final class SettingsPage extends AbstractValueSelector {

    CongasClient client = (CongasClient) CongasCore.getInstance(this);

    /**
     * Array for all settings (except 'back')
     */
    final static String[] settings = {
            "Debug",
            "Send error reports",
            "Back"
    };

    /**
     * Define all settings' return and toggle functions
     * @param index index number of a setting
     * @param toggle toggle value or just return?
     * @return true if setting is enabled
     */
    private boolean toggle(int index, boolean toggle) {
        switch (index) {
            case 0:
                if (toggle) client.enableDebug(!CongasClient.isDebug());
                return CongasClient.isDebug();
            case 1:
                if (toggle) client.enableReport(!CongasClient.reportSendEnabled());
                return CongasClient.reportSendEnabled();
        }
        return false;
    }

    @Override
    public void onCreate(Bundle args) {
        super.onCreate(generate("Settings",
                new Style(Color.FOREST), new Style(Color.BLUE), new Style(Color.PURPLE),
                true,
                settings));
        setHint("Use arrow keys for navigation. Use [Enter] or [Space] to toggle selected");
        addWidget(new TextView("Congas © 2021", new Style(Color.FOREST)))
                .pos().setGravity(Gravity.centerBottom);
        for (int i = 0; i < settings.length - 1; i++)
            updateSetting(i);
    }

    @Override
    protected void selected(String value) {
        if (value.equals("Back")) {
            closeActivity();
            return;
        }
        if (client == null) return;
        for (int i = 0; i < settings.length - 1; i++) {
            if (value.startsWith(settings[i])) {
                toggle(i, true);
                updateSetting(i);
                break;
            }
        }
    }

    /**
     * Update text view
     * @param index index number of setting
     */
    private void updateSetting(int index) {
        if (index < 0 || index >= settings.length - 1)
            throw new IndexOutOfBoundsException();

        updateValue(index, settings[index] + " — " + (toggle(index, false) ? "ON" : "OFF"));
    }

}
