package ru.congas.output.widgets;

/**
 * @author Mr_Told
 */
public class WidgetPosition {

    int offsetX, offsetY;



    private Gravity gravity = Gravity.leftTop;

    public WidgetPosition setGravity(Gravity g) {
        gravity = g;
        return this;
    }

    public WidgetPosition setOffsetX(int offsetX) {
        this.offsetX = offsetX;
        return this;
    }

    public WidgetPosition setOffsetY(int offsetY) {
        this.offsetY = offsetY;
        return this;
    }

    public WidgetPosition setOffset(int offsetX, int offsetY) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        return this;
    }

    public WidgetPosition resetOffset() {
        setOffset(0, 0);
        return this;
    }

    public int getXCoordinate(int widget_width, int total_width) {
        int pos;
        switch (gravity) {
            case leftTop:
            case leftCenter:
            case leftBottom:
                pos = offsetX;
                break;
            case center:
            case centerTop:
            case centerBottom:
                pos = (total_width - widget_width) / 2 + offsetX;
                break;
            default:
                pos = total_width - widget_width + offsetX;
                break;
        }
        return checkCoordinate(pos, total_width);
    }

    public int getYCoordinate(int widget_height, int total_height) {
        int pos;
        switch (gravity) {
            case leftTop:
            case centerTop:
            case rightTop:
                pos = offsetY;
                break;
            case leftCenter:
            case center:
            case rightCenter:
                pos = (total_height - widget_height) / 2 + offsetY;
                break;
            default:
                pos = total_height - widget_height + offsetY;
                break;
        }
        return checkCoordinate(pos, total_height);
    }

    private int checkCoordinate(int val, int max) {
        if (val < 0) return 0;
        return Math.min(val, max);
    }

}
