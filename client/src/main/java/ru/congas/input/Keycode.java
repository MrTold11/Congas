package ru.congas.input;

import java.util.HashMap;
import java.util.Map;

/**
 * Contains special input keys with names
 * @author Mr_Told
**/
public final class Keycode {

    public static final int BACKSPACE  =  8;
    public static final int TAB        =  9;
    public static final int ENTER      = 13;
    public static final int ESCAPE     = 27;
    public static final int SPACE      = 32;

    private static final Map<Integer, String> keyNames = new HashMap<>();

    static {
        keyNames.put(8, "BACKSPACE");
        keyNames.put(9, "TAB");
        keyNames.put(13, "ENTER");
        keyNames.put(27, "ESCAPE");
        keyNames.put(32, "SPACE");
    }

    public static String getKeyName(int k) {
        return keyNames.getOrDefault(k, String.valueOf((char) k));
    }

}
