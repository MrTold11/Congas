package ru.congas.core.output.modifier;

import ru.congas.core.output.canvas.Cell;

import java.util.EnumSet;

/**
 * @author Mr_Told
 */
public class Style {

    protected Integer fg;
    protected Integer bg;
    protected EnumSet<Attribute> attributes;
    
    public Style() {}
    
    public Style(int fg, int bg) {
        this.fg = fg;
        this.bg = bg;
    }
    
    public Style(int bg) {
        this.bg = bg;
    }

    public Style(Style style) {
        if (style == null) return;
        this.bg = style.getBackground();
        this.fg = style.getForeground();
        this.attributes = style.getAttributes().clone();
    }

    public Style    setStyle(Style style) {
        if (style == null) {
            this.bg = null;
            this.fg = null;
            this.attributes = null;
        } else {
            this.bg = style.getBackground();
            this.fg = style.getForeground();
            this.attributes = style.getAttributes() == null ? null : style.getAttributes().clone();
        }
        return this;
    }

    public Style addAttribute(Attribute attribute) {
        if (attributes == null)
            attributes = EnumSet.noneOf(Attribute.class);
        attributes.add(attribute);
        return this;
    }

    public Style removeAttribute(Attribute attribute) {
        if (attributes != null) {
            attributes.remove(attribute);
            if (attributes.size() == 0)
                attributes = null;
        }
        return this;
    }

    public Style clearAttributes() {
        attributes = null;
        return this;
    }

    public Style setColors(int fg, int bg) {
        this.bg = bg;
        this.fg = fg;
        return this;
    }

    public Style setForeground(int fg) {
        this.fg = fg;
        return this;
    }

    public Style setBackground(int bg) {
        this.bg = bg;
        return this;
    }

    public Integer getForeground() {
        return fg;
    }

    public Integer getBackground() {
        return bg;
    }

    public EnumSet<Attribute> getAttributes() {
        return attributes == null ? null : attributes.clone();
    }

}
