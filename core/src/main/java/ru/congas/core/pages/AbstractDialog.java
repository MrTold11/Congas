package ru.congas.core.pages;

import ru.congas.core.application.Bundle;
import ru.congas.core.application.PageActivity;
import ru.congas.core.input.keys.KeyPressed;
import ru.congas.core.output.modifier.Style;
import ru.congas.core.output.widgets.TextView;
import ru.congas.core.output.widgets.properties.Gravity;

/**
 * Simple dialog with two possible answers and a hint at the bottom (optionally)
 * @author Mr_Told
 */
public abstract class AbstractDialog extends PageActivity {

    Style answerStyle, backgroundStyle;

    TextView optionAtv, optionBtv;
    TextView answer;

    public static Bundle generate(String optionA, String optionB, String bottom, Style selected, Style background, String... lines) {
        return new Bundle()
                .addExtra("optionA", optionA).addExtra("optionB", optionB)
                .addExtra("bottom", bottom).addExtra("selected", selected)
                .addExtra("background", background).addExtra("lines", lines);
    }

    @Override
    public void onCreate(Bundle args) {
        String optionA, optionB, bottom;
        Style selected, background;
        String[] lines;
        try {
            optionA    = (String) args.getUnsafeObject("optionA");
            optionB    = (String) args.getUnsafeObject("optionB");
            bottom     = (String) args.getUnsafeObject("bottom");
            selected   = (Style) args.getUnsafeObject("selected");
            background = (Style) args.getUnsafeObject("background");
            lines      = (String[]) args.getUnsafeObject("lines");
        } catch (Exception e) {
            throw new IllegalArgumentException("Dialog cannot be created without arguments", e);
        }
        super.onCreate(args);

        this.answerStyle = selected;
        this.backgroundStyle = background;
        optionAtv = new TextView(optionA, null);
        addWidget(optionAtv)
                .pos().setGravity(Gravity.center).setOffsetX(-2 - optionA.length() / 2).setOffsetY(2);
        optionBtv = new TextView(optionB, null);
        addWidget(optionBtv)
                .pos().setGravity(Gravity.center).setOffsetX(2 + optionA.length() / 2).setOffsetY(2);
        answer = optionAtv;
        answer.setStyle(answerStyle);
        if (bottom != null && bottom.length() > 0)
            addWidget(new TextView(bottom, null))
                    .pos().setGravity(Gravity.centerBottom);

        for (int i = 0; i < lines.length; i++)
            addWidget(new TextView(lines[i], backgroundStyle))
                    .pos().setGravity(Gravity.center).setOffsetY(i - lines.length + 1);
        render();
    }

    @Override
    public boolean handle(KeyPressed event) {
        switch (event.getDefinedKey()) {
            case ENTER:
                if (answer == optionAtv) clickA();
                else clickB();
                render();
                return true;
            case KEY_A:
            case KEY_D:
            case LEFT:
            case RIGHT:
                switchAnswer();
                render();
                return true;
        }
        return false;
    }

    private void switchAnswer() {
        answer.setStyle(null);
        answer = answer == optionAtv ? optionBtv : optionAtv;
        answer.setStyle(answerStyle);
    }

    protected abstract void clickA();

    protected abstract void clickB();

}
