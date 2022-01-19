package ru.congas.input;

import java.util.HashMap;
import java.util.Map;

/**
 * Contains special input keys with names
 * @author Mr_Told
**/
@Deprecated
public final class Keycode {

    public static final int BACKSPACE  =  8;
    public static final int TAB        =  9;
    public static final int ENTER      = 13;
    public static final int ESCAPE     = 27;
    public static final int SPACE      = 32;

    private static final Map<Integer, String> keyNames = new HashMap<>();

    static {
        keyNames.put(BACKSPACE, "BACKSPACE");
        keyNames.put(TAB, "TAB");
        keyNames.put(ENTER, "ENTER");
        keyNames.put(ESCAPE, "ESCAPE");
        keyNames.put(SPACE, "SPACE");
    }

    public static String getKeyName(int k) {
        return keyNames.getOrDefault(k, k < 0 ? "" : String.valueOf((char) k));
    }

    public static String getKeyName(int k, boolean alt, boolean shift, boolean control) {
        if (!alt && !shift && !control) return getKeyName(k);

        StringBuilder sb = new StringBuilder();
        if (alt) sb.append("ALT");
        if (shift) {
            if (sb.length() != 0)
                sb.append(" + ");
            sb.append("SHIFT");
        }
        if (control) {
            if (sb.length() != 0)
                sb.append(" + ");
            sb.append("CTRL");
        }
        String kn = getKeyName(k);
        if (!kn.isEmpty()) {
            if (sb.length() != 0)
                sb.append(" + ");
            sb.append(kn);
        }
        if (sb.length() == 0)
            sb.append("NONE");
        return sb.toString();
    }

}
