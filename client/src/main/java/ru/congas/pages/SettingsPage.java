package ru.congas.pages;

import org.fusesource.jansi.Ansi;
import ru.congas.CongasClient;
import ru.congas.output.widgets.properties.Gravity;
import ru.congas.output.widgets.TextView;

/**
 * @author Mr_Told
 */
public final class SettingsPage extends AbstractValueSelector {

    CongasClient client = null;
    TextView aboutTv = new TextView("Congas © 2021", Ansi.ansi().bgGreen());

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

    public SettingsPage() {
        super("SettingsPage", "SETTINGS", true, false, settings);
        setHint("Use arrow keys for navigation. Use [Enter] or [Space] to toggle selected");
        aboutTv.pos().setGravity(Gravity.centerBottom);
    }

    @Override
    protected void selected(String value) {
        if (value.equals("Back")) {
            CongasClient.back();
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

    public void updateCanvas() {
        super.updateCanvas();
        aboutTv.render(this);
    }

    /**
     * Get client instance for settings control
     * @param client client instance
     */
    public void initClient(CongasClient client) {
        if (client == null) {
            logger.error("CongasClient passed null instance. Exiting settings.");
            CongasClient.back();
            return;
        }
        this.client = client;

        for (int i = 0; i < settings.length - 1; i++)
            updateSetting(i);
    }

    /**
     * Update text view
     * @param index index number of setting
     */
    private void updateSetting(int index) {
        if (index < 0 || index >= settings.length - 1) {
            if (CongasClient.isDebug()) logger.warn("Trying to update setting with index " + index);
            return;
        }
        updateValue(index, settings[index] + " — " + (toggle(index, false) ? "ON" : "OFF"));
    }

}
