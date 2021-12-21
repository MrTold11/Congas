package ru.congas.pages;

import org.fusesource.jansi.Ansi;
import ru.congas.CongasClient;
import ru.congas.output.widgets.Gravity;
import ru.congas.output.widgets.TextView;

/**
 * @author Mr_Told
 */
public final class ErrorScreen extends Page {

    final Ansi blue = Ansi.ansi().bgRgb(8,39,245);
    TextView error, errorText, itsOk, hint;

    public ErrorScreen(String error, String cause) {
        super("ErrorPage", true);
        setOverrideEscape(true);
        this.error = new TextView("Oops! An error has occurred:", blue);
        errorText = new TextView(error + "(" + cause + ")", blue);
        boolean sendReport = CongasClient.reportSendEnabled() && !cause.startsWith("TestAppCrash");
        itsOk = new TextView("It's OK! " + (sendReport ? "Error report will be sent!" : "But error report won't be send :("), blue);
        hint = new TextView("Press any key to continue", Ansi.ansi().bgRgb(255, 255, 255).fgBlack());
        errorText.setPos().setOffsetY(1);
        itsOk.setPos().setOffsetY(2);
        hint.setPos().setGravity(Gravity.centerBottom);
        //todo error report
    }

    @Override
    public boolean handle(int c) {
        CongasClient.openPage(new MainMenu());
        return true;
    }

    @Override
    public void updateCanvas() {
        fillColor(blue);
        error.render(this);
        errorText.render(this);
        itsOk.render(this);
        hint.render(this);
    }
}
