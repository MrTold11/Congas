package ru.congas.core.output.widgets.dynamic;

/**
 * @author Mr_Told
 */
public class DynamicString implements CharSequence {

    String format;
    Object[] values;

    String cached;

    public DynamicString(String format, Object... initValues) {
        this.format = format;
        this.values = initValues;
    }

    @Override
    public int length() {
        if (cached == null) generateString();
        return cached.length();
    }

    @Override
    public char charAt(int index) {
        if (cached == null) generateString();
        return cached.charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        if (cached == null) generateString();
        return cached.subSequence(start, end);
    }

    @Override
    public String toString() {
        if (cached == null) generateString();
        return cached;
    }

    private void generateString() {
        try {
            cached = String.format(format, values);
        } catch (Exception e) {
            cached = e.toString();
        }
    }

    public Object[] editValues() {
        this.cached = null;
        return values;
    }

    public void updateValues(Object... values) {
        this.values = values;
        this.cached = null;
    }

    public void updateFormat(String format) {
        this.format = format;
        this.cached = null;
    }

}
