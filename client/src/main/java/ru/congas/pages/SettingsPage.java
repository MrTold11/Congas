package ru.congas.pages;

import org.fusesource.jansi.Ansi;
import ru.congas.CongasClient;
import ru.congas.output.widgets.Gravity;
import ru.congas.output.widgets.TextView;

/**
 * @author Mr_Told
 */
public final class SettingsPage extends AbstractValueSelector {

    CongasClient client = null;
    TextView aboutTv = new TextView("Congas © 2021 by Mr_Told, Jailflat, DemonTerra", Ansi.ansi().bgGreen());

    public SettingsPage() {
        super("SettingsPage", "SETTINGS", true, false, "Debug", "Back");
        setHint("Use [w]/[s] keys for navigation. Use [Enter] or [Space] to toggle selected");
        aboutTv.setPos().setGravity(Gravity.centerBottom);
    }

    @Override
    protected void selected(String value) {
        if (value.equals("Back")) {
            CongasClient.back();
            return;
        }
        if (client == null) return;
        if (value.contains("Debug")) {
            client.enableDebug(!CongasClient.isDebug());
            updateSetting(0);
        }
    }

    public void updateCanvas() {
        super.updateCanvas();
        aboutTv.render(this);
    }

    public void initClient(CongasClient client) {
        if (client == null) {
            logger.error("CongasClient passed null instance. Exiting settings.");
            CongasClient.back();
            return;
        }
        this.client = client;

        updateSetting(0);
    }

    private void updateSetting(int index) {
        if (index == 0) {
            updateValue(index, "Debug — " + (CongasClient.isDebug() ? "ON" : "OFF"));
        }
    }

}
