package ru.congas.pages;

import ru.congas.core.application.Bundle;
import ru.congas.core.application.PageActivity;
import ru.congas.core.input.keys.KeyPressed;
import ru.congas.core.output.modifier.Color;
import ru.congas.core.output.modifier.Style;
import ru.congas.core.output.widgets.TextView;
import ru.congas.core.output.widgets.properties.Gravity;

/**
 * @author Mr_Told
 */
public abstract class AbstractValueSelector extends PageActivity {

    TextView hintTv = null;
    TextView[] valuesList;
    int current = 0;

    Style optionStyle, selectedStyle;

    protected Bundle generate(String titleText, Style option, Style selected, Style title, boolean hint, String... values) {
        return new Bundle()
                .addExtra("titleText", titleText).addExtra("option", option)
                .addExtra("selected", selected).addExtra("title", title)
                .addExtra("hint", hint).addExtra("values", values);
    }

    public void onCreate(Bundle args) {
        Style titleStyle;
        String titleText;
        String[] values;
        boolean hint;
        try {
            titleText       = (String) args.getUnsafeObject("titleText");
            selectedStyle   = (Style) args.getUnsafeObject("selected");
            optionStyle     = (Style) args.getUnsafeObject("option");
            titleStyle      = (Style) args.getUnsafeObject("title");
            hint            = args.getBoolean("hint", true);
            values          = (String[]) args.getUnsafeObject("values");
        } catch (Exception e) {
            throw new IllegalArgumentException("Dialog cannot be created without arguments", e);
        }
        super.onCreate(args);

        if (hint) setHint("Use arrow keys for navigation. Use [Enter] or [Space] to open selected");
        int offset = hint ? 2 : 0;

        addWidget(new TextView(titleText, titleStyle))
                .pos().setGravity(Gravity.centerTop).setOffsetY(offset);

        offset += 2;
        valuesList = new TextView[values.length];
        for (int i = 0; i < values.length; i++) {
            valuesList[i] = new TextView(values[i] == null ? "null" : values[i], optionStyle);
            addWidget(valuesList[i]).pos().setGravity(Gravity.centerTop).setOffsetY(offset += 2);
        }
        valuesList[current].setStyle(selectedStyle);
        render();
    }

    @Override
    public boolean handle(KeyPressed event) {
        switch (event.getDefinedKey()) {
            case ENTER:
            case SPACE:
                selected(valuesList[current].getText());
                render();
                return true;
            case KEY_W:
            case UP:
                goUp();
                render();
                return true;
            case KEY_S:
            case DOWN:
                goDown();
                render();
                return true;
        }
        return false;
    }

    private void goUp() {
        if (current == 0) return;
        valuesList[current].setStyle(optionStyle);
        current--;
        valuesList[current].setStyle(selectedStyle);
    }

    private void goDown() {
        if (current == valuesList.length - 1) return;
        valuesList[current].setStyle(optionStyle);
        current++;
        valuesList[current].setStyle(selectedStyle);
    }

    protected void setHint(String text) {
        if (hintTv == null) {
            hintTv = new TextView(text, new Style(Color.fromRgb(77, 83, 89)));
            addWidget(hintTv).pos().setGravity(Gravity.leftTop);
        } else hintTv.setText(text);
        render();
    }

    protected void updateValue(int index, String text) {
        if (index < 0 || index >= valuesList.length)
            throw new IndexOutOfBoundsException();

        if (text != null)
            valuesList[index].setText(text);
        render();
    }

    protected abstract void selected(String value);

}
