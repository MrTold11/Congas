package ru.congas.input.keys;

/**
 * Key press event info
 * @author Mr_Told
 */
public class KeyPressed {

    private final AnyKey key;
    private final Character c;
    private final boolean shift, ctrl, alt;

    /**
     * Constructor for unknown key press (system or input char)
     * @param keycode pressed keyboard key
     * @param shift was shift pressed
     * @param ctrl was control pressed
     * @param alt was alt pressed
     */
    public KeyPressed(char keycode, boolean shift, boolean ctrl, boolean alt) {
        if (Character.isLetter(keycode) && Character.isUpperCase(keycode)) {
            this.shift = true;
            keycode = Character.toLowerCase(keycode);
        } else
            this.shift = shift;
        if ((keycode < 58 && keycode > 47))
            key = Key.getKey(keycode);
        else if (keycode < 123 && keycode > 96)
            key = Key.getKey((char) ((int) keycode - 32));
        else switch (keycode) {
                case 8:
                case 9:
                case 13:
                case 27:
                case 32:
                    key = Key.getKey(keycode);
                    break;
                case '*':
                    key = Key.MULTIPLY;
                    break;
                case '+':
                    key = Key.ADD;
                    break;
                case '\\':
                    key = Key.SEPARATOR;
                    break;
                case '-':
                    key = Key.SUBTRACT;
                    break;
                case '/':
                    key = Key.DIVIDE;
                    break;
                case '.':
                    key = Key.DECIMAL;
                    break;
                default:
                    key = new UndefinedKey(keycode);
                    break;
            }
        this.c = Character.isLetterOrDigit(keycode) || "/*!@#$%^&*()\"{}_[]|\\?/<>,.".indexOf(keycode) != -1 ? keycode : null;
        this.ctrl = ctrl;
        this.alt = alt;
    }

    /**
     * Constructor for unknown key press (system or input char)
     * @param key pressed keyboard key
     * @param shift was shift pressed
     * @param ctrl was control pressed
     * @param alt was alt pressed
     */
    public KeyPressed(Key key, boolean shift, boolean ctrl, boolean alt) {
        this(key, Character.isLetterOrDigit(key.getCode()) ? key.getCode() : null, shift, ctrl, alt);
    }

    /**
     * @param key pressed keyboard key
     * @param c input character (null if system key)
     * @param shift was shift pressed
     * @param ctrl was control pressed
     * @param alt was alt pressed
     */
    public KeyPressed(AnyKey key, Character c, boolean shift, boolean ctrl, boolean alt) {
        this.key = key;
        this.c = c;
        this.shift = shift;
        this.ctrl = ctrl;
        this.alt = alt;
    }

    /**
     * @return true if it was character input
     */
    public boolean isCharacter() {
        return c != null;
    }

    /**
     * @return input character (or null if it was system key press)
     */
    public Character getChar() {
        return c;
    }

    /**
     * @return character that supposed to be printed
     */
    public Character getFormattedChar() {
        return shift && isCharacter() ? Character.toUpperCase(getChar()) : getChar();
    }

    /**
     * @return keyboard key
     */
    public AnyKey getSystemKey() {
        return key;
    }

    public Key getDefinedKey() {
        return key instanceof Key ? (Key) key : Key.NONE;
    }

    /**
     * @return true if shift was pressed
     */
    public boolean isShift() {
        return shift || key == Key.SHIFT;
    }

    /**
     * @return true if control was pressed
     */
    public boolean isCtrl() {
        return ctrl || key == Key.CONTROL;
    }

    /**
     * @return true if alt was pressed
     */
    public boolean isAlt() {
        return alt || key == Key.ALT;
    }

    /**
     * @return true if alt, shift or control has been pressed
     */
    public boolean isAltShiftControl() {
        return isAlt() || isShift() || isCtrl();
    }

    /**
     * @return human-readable format for this event
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (alt && key != Key.ALT) sb.append("ALT + ");
        if (shift && key != Key.SHIFT) sb.append("SHIFT + ");
        if (ctrl && key != Key.CONTROL) sb.append("CTRL + ");
        if (key != null) {
            sb.append(key.getName());
            if (isCharacter() && (c != key.getCode() || key instanceof UndefinedKey))
                sb.append('(').append(c).append(')');
        } else {
            sb.append("NULL");
            if (isCharacter()) sb.append('<').append((int) c).append('>');
        }
        return sb.toString();
    }

}
