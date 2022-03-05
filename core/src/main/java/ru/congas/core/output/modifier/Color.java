package ru.congas.core.output.modifier;

/**
 * @author Mr_Told
 */
public class Color {

    public static final int WHITE = fromRgb( 255, 255, 255);
    public static final int LIGHT_GRAY = 0xbfbfbf;
    public static final int GRAY = 0x7f7f7f;
    public static final int DARK_GRAY = 0x3f3f3f;
    public static final int BLACK = fromRgb(0, 0, 0);

    public static final int BLUE = fromRgb(0, 0, 255);
    public static final int NAVY = fromRgb(0, 0, 127);
    public static final int ROYAL = 0x4169e1;
    public static final int SLATE = 0x708090;
    public static final int SKY = 0x87ceeb;
    public static final int CYAN = fromRgb(0, 255, 255);
    public static final int TEAL = fromRgb(0, 127, 127);

    public static final int GREEN = 0x00ff00;
    public static final int CHARTREUSE = 0x7fff00;
    public static final int LIME = 0x32cd32;
    public static final int FOREST = 0x228b22;
    public static final int OLIVE = 0x6b8e23;

    public static final int YELLOW = 0xffff00;
    public static final int GOLD = 0xffd700;
    public static final int GOLDENROD = 0xdaa520;
    public static final int ORANGE = 0xffa500;

    public static final int BROWN = 0x8b4513;
    public static final int TAN = 0xd2b48c;
    public static final int FIREBRICK = 0xb22222;

    public static final int RED = fromRgb(255, 0, 0);
    public static final int SCARLET = 0xff341c;
    public static final int CORAL = 0xff7f50;
    public static final int SALMON = 0xfa8072;
    public static final int PINK = 0xff69b4;
    public static final int MAGENTA = fromRgb( 255, 0, 255);

    public static final int PURPLE = 0xa020f0;
    public static final int VIOLET = 0xee82ee;
    public static final int MAROON = 0xb03060;

    public static int fromHex(String hex) {
        if (hex.charAt(0) == '#') {
            long color = Long.parseLong(hex.substring(1), 16);
            if (hex.length() == 7)
                color |= 0x00000000ff000000;
            else if (hex.length() != 9)
                throw new IllegalArgumentException("Unknown color");

            return (int) color;
        }

        throw new IllegalArgumentException("Unknown color");
    }

    public static int fromRgb(int r, int g, int b) {
        if (r < 0 || r > 255
                || g < 0 || g > 255
                || b < 0 || b > 255)
            throw new IllegalArgumentException("Values must be between 0 and 255");

        return 0xff000000 | (r << 16) | (g << 8) | b;
    }

}
