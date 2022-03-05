package ru.congas.core.output.canvas;

import org.apache.logging.log4j.LogManager;

/**
 * @author Mr_Told
 */
public class Frame {

    volatile String frame;
    volatile int frameHeight, frameWidth;
    volatile boolean ready = false;

    public synchronized void set(String f, int h, int w) {
        frame = f;
        frameHeight = h;
        frameWidth = w;
        ready = true;
    }

    public synchronized Object[] get() {
        return new Object[]{frame, frameHeight, frameWidth};
    }

}
