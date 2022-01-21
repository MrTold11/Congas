package ru.congas.pages;

import org.fusesource.jansi.Ansi;
import ru.congas.CongasClient;
import ru.congas.input.keys.KeyPressed;
import ru.congas.output.widgets.properties.Gravity;
import ru.congas.output.widgets.TextView;

import java.io.File;
import java.io.PrintWriter;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Mr_Told
 */
public final class ErrorScreen extends Page {

    final Ansi blue = Ansi.ansi().bgRgb(8,39,245);
    TextView error, errorText, itsOk, hint;

    private static final AtomicInteger fatalCounter = new AtomicInteger(0);

    public ErrorScreen(String error, String cause, Exception e) {
        super("ErrorPage", false);
        checkFatal(error, cause, e);
        setOverrideEscape(true);
        this.error = new TextView("Oops! An error has occurred:", blue);
        errorText = new TextView(error + "(" + cause + ")", blue);
        boolean sendReport = CongasClient.reportSendEnabled() && !cause.startsWith("TestAppCrash");
        itsOk = new TextView("It's OK! " + (sendReport ? "Error report will be sent!" : "But error report won't be send :("), blue);
        hint = new TextView("Press any key to continue", Ansi.ansi().bgRgb(255, 255, 255).fgBlack());
        errorText.pos().setOffsetY(1);
        itsOk.pos().setOffsetY(2);
        hint.pos().setGravity(Gravity.centerBottom);
        //todo error report
    }

    private void checkFatal(String error, String cause, Exception err) {
        int fatal = fatalCounter.incrementAndGet();
        if (fatal > 50) {
            try {
                logger.fatal("Too many crashes! Stopping CongasClient!");
                if (fatal == 51) {
                    try {
                        File crashDir = new File(CongasClient.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParentFile();
                        crashDir = new File(crashDir, "crashes");
                        if (!crashDir.exists()) //noinspection ResultOfMethodCallIgnored
                            crashDir.mkdirs();
                        if (crashDir.isDirectory() && crashDir.exists()) {
                            try (PrintWriter crashPrintWriter = new PrintWriter(new File(crashDir, "crash_" + System.currentTimeMillis() + ".txt"))) {
                                crashPrintWriter.write("Extreme crash has happened: ");
                                crashPrintWriter.write(error);
                                crashPrintWriter.write("\nCause: ");
                                crashPrintWriter.write(cause);
                                crashPrintWriter.write("\nTrace: ");
                                err.printStackTrace(crashPrintWriter);
                            }
                        }
                        CongasClient.close();
                        System.exit(88888886);
                    } catch (Exception e) {
                        logger.fatal("Error during crash save: ", e);
                        e.printStackTrace();
                        System.exit(88888887);
                    }
                } else
                    System.exit(88888888);
            } catch (Exception ignored) {}

            System.exit(88888889);
        }
    }

    @Override
    public boolean handle(KeyPressed event) {
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
