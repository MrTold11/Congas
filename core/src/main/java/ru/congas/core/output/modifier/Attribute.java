package ru.congas.core.output.modifier;

/**
 * @author Mr_Told
 */
public enum Attribute {

    RESET(0),
    INTENSITY_BOLD_OFF(22),
    ITALIC_OFF(23),
    UNDERLINE_OFF(24),
    BLINK_OFF(25),
    NEGATIVE_OFF(27),
    CONCEAL_OFF(28),
    STRIKETHROUGH_OFF(29),
    INTENSITY_BOLD(1, INTENSITY_BOLD_OFF),
    INTENSITY_FAINT(2, INTENSITY_BOLD_OFF),
    ITALIC(3, ITALIC_OFF),
    UNDERLINE(4, UNDERLINE_OFF),
    BLINK_SLOW(5, BLINK_OFF),
    BLINK_FAST(6, BLINK_OFF),
    NEGATIVE_ON(7, NEGATIVE_OFF),
    CONCEAL_ON(8, CONCEAL_OFF),
    STRIKETHROUGH_ON(9, STRIKETHROUGH_OFF),
    UNDERLINE_DOUBLE(21, UNDERLINE_OFF);

    private final int value;
    private final Attribute anti;

    Attribute(int value) {
        this.value = value;
        this.anti = this;
    }

    Attribute(int value, Attribute anti) {
        this.value = value;
        this.anti = anti;
    }

    public int getValue() {
        return value;
    }

    public Attribute getAntiValue() {
        return anti;
    }

    public boolean neutral() {
        return anti == this;
    }
}
