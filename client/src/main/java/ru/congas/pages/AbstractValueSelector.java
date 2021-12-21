package ru.congas.pages;

import org.fusesource.jansi.Ansi;
import ru.congas.input.Keycode;
import ru.congas.output.widgets.Gravity;
import ru.congas.output.widgets.TextView;

/**
 * @author Mr_Told
 */
public abstract class AbstractValueSelector extends Page {

    TextView titleTv, hintTv = null;
    TextView[] valuesList;
    int current = 0;

    Ansi bg = Ansi.ansi().bgYellow();
    Ansi sel = Ansi.ansi().bgCyan();

    public AbstractValueSelector(String name, String title, boolean hint, boolean temporary, String... values) {
        super(name, temporary);
        if (hint) setHint("Use [w]/[s] keys for navigation. Use [Enter] or [Space] to open selected");
        int align = hint ? 2 : 0;

        titleTv = new TextView(title, Ansi.ansi().bgMagenta());
        titleTv.setPos().setGravity(Gravity.centerTop).setOffsetY(align);

        align += 2;
        valuesList = new TextView[values.length];
        for (int i = 0; i < values.length; i++) {
            valuesList[i] = new TextView(values[i] == null ? "null" : values[i], bg);
            valuesList[i].setPos().setGravity(Gravity.centerTop).setOffsetY(align += 2);
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

    @Override
    public void updateCanvas() {
        titleTv.render(this);
        if (hintTv != null) hintTv.render(this);
        for (TextView tv : valuesList)
            tv.render(this);
    }

    protected void setHint(String text) {
        if (hintTv == null) {
            hintTv = new TextView(text, Ansi.ansi().bgRgb(77, 83, 89));
            hintTv.setPos().setGravity(Gravity.leftTop);
        } else hintTv.setText(text);
    }

    protected void updateValue(int index, String text) {
        if (index < 0 || index >= valuesList.length) {
            logger.warn(getName() + " trying to update value on non-existing index " + index
                    + ". Value: " + (text == null ? "null" : text.substring(0, Math.min(16, text.length()))));
            return;
        }

        if (text != null) valuesList[index].setText(text);
        forceUpdate();
    }

    protected abstract void selected(String value);

}
