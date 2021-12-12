package ru.congas.pages;

import org.fusesource.jansi.Ansi;
import ru.congas.SimpleGame;
import ru.congas.input.Keycode;
import ru.congas.output.widgets.Gravity;
import ru.congas.output.widgets.TextView;

/**
 * @author Mr_Told
 */
public abstract class MenuSelector extends SimpleGame {

    TextView titleTv, hintTv = null;
    TextView[] valuesList;
    int current = 0;

    Ansi bg = Ansi.ansi().bgYellow();
    Ansi sel = Ansi.ansi().bgCyan();

    public MenuSelector(String name, String title, boolean hint, String... values) {
        super(name, false, false,
                true, false, 10, 10, 10);
        if (hint) {
            hintTv = new TextView("Use [w]/[s] keys for navigation. Use [Enter] or [Space] to open selected", Ansi.ansi().bgRgb(77, 83, 89));
            hintTv.setPos().setGravity(Gravity.leftTop);
        }
        int align = hint ? 2 : 0;

        titleTv = new TextView(title, Ansi.ansi().bgMagenta());
        titleTv.setPos().setGravity(Gravity.centerTop).setAlignY(align);

        align += 2;
        valuesList = new TextView[values.length];
        for (int i = 0; i < values.length; i++) {
            valuesList[i] = new TextView(values[i], bg);
            valuesList[i].setPos().setGravity(Gravity.centerTop).setAlignY(align += 2);
        }
        valuesList[current].setColors(sel);
    }

    @Override
    public boolean handle(int c) {
        switch (c) {
            case Keycode.ENTER:
            case Keycode.SPACE:
                selected(valuesList[current].getText());
                forceUpdate();
                return true;
            case 'w':
            case 'ц':
                goUp();
                forceUpdate();
                return true;
            case 's':
            case 'ы':
                goDown();
                forceUpdate();
                return true;
        }
        return false;
    }

    private void goUp() {
        if (current == 0) return;
        valuesList[current].setColors(bg);
        current--;
        valuesList[current].setColors(sel);
    }

    private void goDown() {
        if (current == valuesList.length - 1) return;
        valuesList[current].setColors(bg);
        current++;
        valuesList[current].setColors(sel);
    }

    public void resized(int w, int h) {
        initCanvas(w, h);
    }

    @Override
    public void updateCanvas() {
        titleTv.render(this);
        if (hintTv != null) hintTv.render(this);
        for (TextView tv : valuesList)
            tv.render(this);
    }

    protected abstract void selected(String value);

}
