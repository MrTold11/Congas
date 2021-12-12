package ru.congas.pages;

import org.fusesource.jansi.Ansi;
import ru.congas.SimpleGame;
import ru.congas.input.Keycode;
import ru.congas.output.widgets.Gravity;
import ru.congas.output.widgets.TextView;

/**
 * Simple dialog with two possible answers and a hint at the bottom (optionally)
 * @author Mr_Told
 */
public abstract class Dialog extends SimpleGame {

    Ansi answerColor = Ansi.ansi().bgCyan();
    Ansi bg = Ansi.ansi().bgYellow();

    TextView optionAtv, optionBtv, bottomTv = null;
    TextView[] lines;

    TextView answer;

    public Dialog(String optionA, String optionB, String bottom, String... lines) {
        super("dialog", false, false,
                true, false, 10, 20, 3 + lines.length);
        optionAtv = new TextView(optionA, null);
        optionAtv.setPos().setGravity(Gravity.center).setAlignX(-2 - optionA.length() / 2).setAlignY(2);
        optionBtv = new TextView(optionB, null);
        optionBtv.setPos().setGravity(Gravity.center).setAlignX(2 + optionA.length() / 2).setAlignY(2);
        answer = optionAtv;
        if (bottom != null && bottom.length() > 0) {
            bottomTv = new TextView(bottom, null);
            bottomTv.setPos().setGravity(Gravity.centerBottom);
        }
        this.lines = new TextView[lines.length];
        for (int i = 0; i < lines.length; i++) {
            this.lines[i] = new TextView(lines[i], bg);
            this.lines[i].setPos().setGravity(Gravity.center).setAlignY(i - lines.length + 1);
        }
    }

    @Override
    public boolean handle(int c) {
        switch (c) {
            case Keycode.ENTER:
                if (answer == optionAtv) clickA();
                else clickB();
                forceUpdate();
                return true;
            case 'a':
            case 'ф':
            case 'd':
            case 'в':
                switchAnswer();
                forceUpdate();
                return true;
        }
        return false;
    }

    private void switchAnswer() {
        answer.setColors(null);
        answer = answer == optionAtv ? optionBtv : optionAtv;
        answer.setColors(answerColor);
    }

    public void resized(int w, int h) {
        initCanvas(w, h);
    }

    @Override
    public void updateCanvas() {
        optionAtv.render(this);
        optionBtv.render(this);
        for (TextView tv : lines)
            tv.render(this);
        if (bottomTv != null) bottomTv.render(this);
    }

    protected abstract void clickA();

    protected abstract void clickB();

}
