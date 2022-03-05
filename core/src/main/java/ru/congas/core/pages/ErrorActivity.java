package ru.congas.core.pages;

import ru.congas.core.CongasCore;
import ru.congas.core.application.Bundle;
import ru.congas.core.application.PageActivity;
import ru.congas.core.input.keys.KeyPressed;
import ru.congas.core.output.modifier.Color;
import ru.congas.core.output.modifier.Style;
import ru.congas.core.output.widgets.TextView;
import ru.congas.core.output.widgets.properties.Gravity;

import java.io.File;
import java.io.PrintWriter;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Mr_Told
 */
public class ErrorActivity extends PageActivity {

    final Style blue = new Style(Color.fromRgb(8,39,245));

    String err = "???", cause = "???";
    Exception e;

    private static final AtomicInteger fatalCounter = new AtomicInteger(0);

    @Override
    public void onCreate(Bundle args) {
        super.onCreate(args);
        if (args != null) {
            err = args.getString("err", "???");
            cause = args.getString("cause", "???");
            e = (Exception) args.getObject("exception", Exception.class, null);
        }
        checkFatal(err, cause, e);
        addWidget(new TextView("Oops! An error has occurred:", blue));
        addWidget(new TextView(err + "(" + cause + ")", blue)).pos().setOffsetY(1);
        boolean sendReport = CongasCore.reportSendEnabled() && !cause.startsWith("TestAppCrash");
        addWidget(new TextView("It's OK! " + (sendReport ? "Error report will be sent!" : "But error report won't be send :("), blue))
                .pos().setOffsetY(2);
        addWidget(new TextView("Press any key to continue", new Style(Color.WHITE, Color.BLACK)))
                .pos().setGravity(Gravity.centerBottom);
    }

    private void checkFatal(String error, String cause, Exception err) {
        int fatal = fatalCounter.incrementAndGet();
        if (fatal > 50) {
            try {
                logger.fatal("Too many crashes! Stopping CongasCore!");
                if (fatal == 51) {
                    try {
                        File crashDir = new File(CongasCore.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParentFile();
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
                                if (err != null)
                                    err.printStackTrace(crashPrintWriter);
                            }
                        }
                        CongasCore.close(this);
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
        if (event.isAltShiftControl()) return false;
        closeActivity();
        return true;
    }

    @Override
    protected void render() {
        getCanvas().fill(blue, ' ');
        super.render();
    }

}
