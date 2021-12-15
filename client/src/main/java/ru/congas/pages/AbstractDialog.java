package ru.congas.pages;

import org.fusesource.jansi.Ansi;
import ru.congas.input.Keycode;
import ru.congas.output.widgets.Gravity;
import ru.congas.output.widgets.TextView;

/**
 * Simple dialog with two possible answers and a hint at the bottom (optionally)
 * @author Mr_Told
 */
public abstract class AbstractDialog extends Page {

    Ansi answerColor = Ansi.ansi().bgCyan();
    Ansi bg = Ansi.ansi().bgYellow();

    TextView optionAtv, optionBtv, bottomTv = null;
    TextView[] lines;

    TextView answer;

    public AbstractDialog(String optionA, String optionB, String bottom, boolean temporary, String... lines) {
        super("Dialog_" +
                (lines.length > 0
                        ? lines[0].substring(0, Math.min(9, lines[0].length()))
                        : "empty"),
                temporary);
        optionAtv = new TextView(optionA, null);
        optionAtv.setPos().setGravity(Gravity.center).setOffsetX(-2 - optionA.length() / 2).setOffsetY(2);
        optionBtv = new TextView(optionB, null);
        optionBtv.setPos().setGravity(Gravity.center).setOffsetX(2 + optionA.length() / 2).setOffsetY(2);
        answer = optionAtv;
        if (bottom != null && bottom.length() > 0) {
            bottomTv = new TextView(bottom, null);
            bottomTv.setPos().setGravity(Gravity.centerBottom);
        }
        this.lines = new TextView[lines.length];
        for (int i = 0; i < lines.length; i++) {
            this.lines[i] = new TextView(lines[i], bg);
            this.lines[i].setPos().setGravity(Gravity.center).setOffsetY(i - lines.length + 1);
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
