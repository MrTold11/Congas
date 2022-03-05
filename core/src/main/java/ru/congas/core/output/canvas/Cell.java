package ru.congas.core.output.canvas;

import ru.congas.core.output.modifier.Attribute;
import ru.congas.core.output.modifier.Style;

import java.util.EnumSet;

/**
 * @author Mr_Told
 */
public class Cell extends Style {

    private static final char FIRST_ESC_CHAR = 27;
    private static final char SECOND_ESC_CHAR = '[';
    private static final char SEPARATOR = ';';

    private char c = ' ';

    private final EnumSet<Attribute> temp = EnumSet.noneOf(Attribute.class);

    public Cell() {}

    public Cell(char c) {
        this.c = c;
    }

    public Cell(char c, int fg) {
        this.c = c;
    }

    public Cell(char c, int fg, int bg) {
        super(fg, bg);
        this.c = c;
    }

    public Cell(int bg) {
        super(bg);
    }

    public Cell(Style style) {
        if (style == null) return;
        this.bg = style.getBackground();
        this.fg = style.getForeground();
        this.attributes = style.getAttributes().clone();
    }

    public Cell setChar(char c) {
        this.c = c;
        return this;
    }

    public char getChar() {
        return c;
    }

    private void appendColor(StringBuilder sb, int hex) {
        sb.append("2;");
        sb.append((hex >> 16) & 0xff).append(SEPARATOR);
        sb.append((hex >> 8) & 0xff).append(SEPARATOR);
        sb.append(hex & 0xff).append(SEPARATOR);
    }

    public Cell render(Cell previous, StringBuilder target) {
        if (previous == null) {
            if (bg == null && fg == null && attributes == null)
                target.append(c);
            else {
                target.append(FIRST_ESC_CHAR).append(SECOND_ESC_CHAR);
                if (fg != null) {
                    target.append("38;");
                    appendColor(target, fg);
                }

                if (bg != null) {
                    target.append("48;");
                    appendColor(target, bg);
                }

                if (attributes != null) {
                    for (Attribute attribute : attributes)
                        target.append(attribute.getValue()).append(SEPARATOR);
                }
                target.setLength(Math.max(target.length() - 1, 0));
                target.append('m');
                target.append(c);
            }
            return this;
        }

        if (previous == this) {
            target.append(c);
            return this;
        }

        boolean rst = (fg == null && previous.fg != null)
                || (bg == null && previous.bg != null)
                || (attributes != null && attributes.contains(Attribute.RESET));

        //As we use EnumSet, it iterates in order in which attributes declared into enum
        //so RESET operation will be the first
        EnumSet<Attribute> deltaAttr = deltaAttr(previous.attributes, attributes, rst);
        boolean updFg = needUpdate(fg, previous.fg);
        boolean updBg = needUpdate(bg, previous.bg);

        if (updBg || updFg || deltaAttr != null || rst) {
            target.append(FIRST_ESC_CHAR).append(SECOND_ESC_CHAR);
            if (rst)
                target.append("0;");

            if (updBg) {
                target.append("48;");
                appendColor(target, bg);
            }

            if (updFg) {
                target.append("38;");
                appendColor(target, fg);
            }

            if (deltaAttr != null) {
                for (Attribute attribute : deltaAttr)
                    target.append(attribute.getValue()).append(SEPARATOR);
            }

            target.setLength(Math.max(target.length() - 1, 0));
            target.append('m');
        }

        target.append(c);
        return this;
    }

    private boolean needUpdate(Integer i1, Integer i2) {
        if (i1 == null) return false;
        if (i2 == null) return true;
        return i1.intValue() != i2.intValue();
    }

    private EnumSet<Attribute> deltaAttr(EnumSet<Attribute> old, EnumSet<Attribute> nw, boolean reset) {
        if (old == null || reset)
            return nw;
        temp.clear();
        if (nw == null) {
            for (Attribute a : old) {
                if (a.neutral())
                    continue;
                temp.add(a.getAntiValue());
            }
        } else {
            for (Attribute a : old) {
                if (a.neutral() || nw.contains(a))
                    continue;
                temp.add(a.getAntiValue());
            }
        }

        return temp;
    }

}
