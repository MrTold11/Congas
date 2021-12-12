package ru.congas.output.widgets;

/**
 * @author Mr_Told
 */
public class WidgetPosition {

    int alignX, alignY;



    private Gravity gravity = Gravity.leftTop;

    public WidgetPosition setGravity(Gravity g) {
        gravity = g;
        return this;
    }

    public WidgetPosition setAlignX(int alignX) {
        this.alignX = alignX;
        return this;
    }

    public WidgetPosition setAlignY(int alignY) {
        this.alignY = alignY;
        return this;
    }

    public WidgetPosition setAlign(int alignX, int alignY) {
        this.alignX = alignX;
        this.alignY = alignY;
        return this;
    }

    public WidgetPosition resetAlign() {
        setAlign(0, 0);
        return this;
    }

    public int getXCoordinate(int widget_width, int total_width) {
        int pos;
        switch (gravity) {
            case leftTop:
            case leftCenter:
            case leftBottom:
                pos = alignX;
                break;
            case center:
            case centerTop:
            case centerBottom:
                pos = (total_width - widget_width) / 2;
                break;
            default:
                pos = total_width - widget_width;
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
                pos = alignY;
                break;
            case leftCenter:
            case center:
            case rightCenter:
                pos = (total_height - widget_height) / 2;
                break;
            default:
                pos = total_height - widget_height;
                break;
        }
        return checkCoordinate(pos, total_height);
    }

    private int checkCoordinate(int val, int max) {
        if (val < 0) return 0;
        return Math.min(val, max);
    }

}
