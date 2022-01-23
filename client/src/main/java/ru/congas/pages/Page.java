package ru.congas.pages;

import ru.congas.SimpleApp;

/**
 * @author Mr_Told
 */
public abstract class Page extends SimpleApp {

    boolean temporary;

    /**
     * @param name page's name
     * @param temporary true if this page shouldn't be opened on back() function (temporary page for decision/game pause)
     */
    public Page(String name, boolean temporary) {
        super(name, true, false, 10, 10, 10);
        this.temporary = temporary;
    }

    public void resized(int w, int h) {
        initCanvas(w, h);
    }

    public boolean isTemporary() {
        return temporary;
    }

}
