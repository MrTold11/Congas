package ru.congas.pages;

import org.fusesource.jansi.Ansi;
import ru.congas.CongasClient;
import ru.congas.input.keys.Key;
import ru.congas.input.keys.KeyPressed;
import ru.congas.output.widgets.properties.Gravity;
import ru.congas.output.widgets.TextView;

/**
 * Page appears if the game that should be in the anthology somehow isn't loaded
 * @author Mr_Told
 */
public final class AppNotFound extends Page {

    TextView notFoundText, pressToContinue;

    public AppNotFound(String gameName, String packageName) {
        super("AppNotFound", true);
        notFoundText = new TextView("Application " + gameName + " not found in anthology " + packageName, Ansi.ansi().bgRed());
        notFoundText.pos().setGravity(Gravity.center);
        pressToContinue = new TextView("Press [Space] or [Enter] to continue", null);
        pressToContinue.pos().setGravity(Gravity.centerBottom);
    }

    @Override
    public boolean handle(KeyPressed event) {
        if (event.getDefinedKey() == Key.SPACE || event.getDefinedKey() == Key.ENTER) {
            CongasClient.back();
            return true;
        }
        return false;
    }

    @Override
    public void updateCanvas() {
        notFoundText.render(this);
        pressToContinue.render(this);
    }

}
