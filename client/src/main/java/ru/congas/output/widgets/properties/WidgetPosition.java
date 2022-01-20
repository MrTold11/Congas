package ru.congas.output.widgets.properties;

/**
 * @author Mr_Told
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public class WidgetPosition {

    int offsetX, offsetY;
    int width, height, widthScale = 1, heightScale = 1;

    private Gravity gravity = Gravity.leftTop;
    private WidgetSizeType widthType = WidgetSizeType.wrap_content;
    private WidgetSizeType heightType = WidgetSizeType.wrap_content;

    public WidgetPosition setScale(int widthScale, int heightScale) {
        if (widthScale < 0 || heightScale < 0) return this;
        this.widthScale = widthScale;
        this.heightScale = heightScale;
        return this;
    }

    public WidgetPosition setWidthScale(int widthScale) {
        if (widthScale < 0) return this;
        this.widthScale = widthScale;
        return this;
    }

    public WidgetPosition setHeightScale(int heightScale) {
        if (heightScale < 0) return this;
        this.heightScale = heightScale;
        return this;
    }

    public WidgetPosition setCustomSize(int width, int height) {
        if (width < 0 || height < 0) return this;
        this.width = width;
        this.height = height;
        this.widthType = WidgetSizeType.custom;
        this.heightType = WidgetSizeType.custom;
        return this;
    }

    public WidgetPosition setWidgetWidth(int width) {
        if (width < 0) return this;
        this.width = width;
        this.widthType = WidgetSizeType.custom;
        return this;
    }

    public WidgetPosition setWidgetHeight(int height) {
        if (height < 0) return this;
        this.height = height;
        this.heightType = WidgetSizeType.custom;
        return this;
    }

    public WidgetPosition setSizeType(WidgetSizeType widthType, WidgetSizeType heightType) {
        this.widthType = widthType;
        this.heightType = heightType;
        return this;
    }

    public WidgetPosition setWidthType(WidgetSizeType widthType) {
        this.widthType = widthType;
        return this;
    }

    public WidgetPosition setHeightType(WidgetSizeType heightType) {
        this.heightType = heightType;
        return this;
    }

    public int getEndXCoordinate(int startX, int contentWidth, int parentWidth) {
        int mWidth = getMaxWidth(contentWidth, parentWidth);
        return checkCoordinate(startX + mWidth, parentWidth);
    }

    public int getEndXCoordinate(int contentWidth, int parentWidth) {
        return getEndXCoordinate(getXCoordinate(contentWidth, parentWidth), contentWidth, parentWidth);
    }

    public int getEndYCoordinate(int startY, int contentHeight, int parentHeight) {
        int mHeight = getMaxHeight(contentHeight, parentHeight);
        return checkCoordinate(startY + mHeight, parentHeight);
    }

    public int getEndYCoordinate(int contentHeight, int parentHeight) {
        return getEndYCoordinate(getYCoordinate(contentHeight, parentHeight), contentHeight, parentHeight);
    }

    private int getMaxWidth(int contentWidth, int parentWidth) {
        int width = widthScale;
        switch (widthType) {
            case wrap_content:
                width *= contentWidth;
                break;
            case match_parent:
                width *= parentWidth;
                break;
            case custom:
                width *= this.width;
        }
        return checkCoordinate(width, parentWidth);
    }

    private int getMaxHeight(int contentHeight, int parentHeight) {
        int height = heightScale;
        switch (heightType) {
            case wrap_content:
                height *= contentHeight;
                break;
            case match_parent:
                height *= parentHeight;
                break;
            case custom:
                height *= this.height;
        }
        return checkCoordinate(height, parentHeight);
    }

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

    public int getContentX(int contentWidth, int parentWidth) {
        return calculateX(Math.min(contentWidth, getMaxWidth(contentWidth, parentWidth)), parentWidth);
    }

    public int getContentY(int contentHeight, int parentHeight) {
        return calculateY(Math.min(contentHeight, getMaxHeight(contentHeight, parentHeight)), parentHeight);
    }

    public int getContentWidth(int contentWidth, int parentWidth) {
        return Math.min(contentWidth, getMaxWidth(contentWidth, parentWidth));
    }

    public int getXCoordinate(int contentWidth, int parentWidth) {
        return calculateX(getMaxWidth(contentWidth, parentWidth), parentWidth);
    }

    public int getYCoordinate(int contentHeight, int parentHeight) {
        return calculateY(getMaxHeight(contentHeight, parentHeight), parentHeight);
    }

    private int calculateY(int value, int parent) {
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
                pos = (parent - value) / 2 + offsetY;
                break;
            default:
                pos = parent - value + offsetY;
                break;
        }
        return checkCoordinate(pos, parent);
    }

    private int calculateX(int value, int parent) {
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
                pos = (parent - value) / 2 + offsetX;
                break;
            default:
                pos = parent - value + offsetX;
                break;
        }
        return checkCoordinate(pos, parent);
    }

    private int checkCoordinate(int val, int max) {
        if (val < 0) return 0;
        return Math.min(val, max - 1);
    }

}
