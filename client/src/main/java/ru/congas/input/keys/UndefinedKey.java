package ru.congas.input.keys;

/**
 * @author Mr_Told
 */
public class UndefinedKey implements AnyKey {

    final char code;

    public UndefinedKey(char code) {
        this.code = code;
    }

    @Override
    public char getCode() {
        return code;
    }

    @Override
    public String getName() {
        return "UNDEFINED_" + (int) code;
    }
}
