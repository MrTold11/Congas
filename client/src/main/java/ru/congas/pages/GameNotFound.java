package ru.congas.pages;

import org.fusesource.jansi.Ansi;
import ru.congas.SimpleGame;
import ru.congas.input.Keycode;
import ru.congas.output.widgets.Gravity;
import ru.congas.output.widgets.TextView;

/**
 * Page appears if the game that should be in the anthology somehow isn't loaded
 * @author Mr_Told
 */
public class GameNotFound extends SimpleGame {

    TextView notFoundText, pressToContinue;

    public GameNotFound(String gameName, String packageName) {
        super("Game not found", false, false,
                true, false, 10, 10, 10);
        notFoundText = new TextView("Game " + gameName + " not found in anthology " + packageName, Ansi.ansi().bgRed());
        notFoundText.setPos().setGravity(Gravity.center);
        pressToContinue = new TextView("Press [Space] or [Enter] to continue", null);
        pressToContinue.setPos().setGravity(Gravity.horizontal_centerBottom);
    }

    @Override
    public boolean handle(int c) {
        if (c == Keycode.SPACE || c == Keycode.ENTER) {
            exit();
            return true;
        }
        return false;
    }

    public void resized(int w, int h) {
        initCanvas(w, h);
    }

    @Override
    public void updateCanvas() {
        notFoundText.render(this);
        pressToContinue.render(this);
    }

}
