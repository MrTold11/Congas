package ru.congas.core.output;

import ru.congas.core.application.Activity;
import ru.congas.core.output.canvas.Canvas;
import ru.congas.core.output.canvas.FinalCanvas;
import ru.congas.core.output.canvas.Frame;

import static ru.congas.core.exception.CalledFromWrongThreadException.checkThread;

/**
 * @author Mr_Told
 */
public class ScreenBuffer {

    private final Activity activity;
    private final Canvas active;
    private final FinalCanvas render;

    private volatile boolean needUpdate = true;
    private volatile boolean liveUpdate = false;
    //todo partial update

    private volatile int fps = 60;
    private volatile int loopTime = 1000 / fps;
    private int terminalWidth, terminalHeight;

    public ScreenBuffer(Activity activity) {
        this(activity, RenderThread.getTerminalWidth(), RenderThread.getTerminalHeight());
    }

    public ScreenBuffer(Activity activity, int w, int h) {
        this.activity = activity;
        this.active = new Canvas(w, h);
        this.render = new FinalCanvas(active);
    }

    public Frame getFrame(RenderThread renderThread) {
        checkThread(renderThread);
        needUpdate = false;
        return render.getFrame();
    }

    public Canvas getCanvas() {
        activity.checkThread();
        return active;
    }

    public void updateCanvas() {
        activity.checkThread();
        needUpdate = true;
        render.build();
    }

    public void updateSize(RenderThread thread, int w, int h) {
        checkThread(thread);
        int oldW = terminalWidth, oldH = terminalHeight;
        activity.runOnUiThread(() -> activity.onResize(oldW, oldH, w, h));
        terminalWidth = w;
        terminalHeight = h;
        //todo multiplexer
        needUpdate = true;
    }

    public int getWidth() {
        return terminalWidth;
    }

    public int getHeight() {
        return terminalHeight;
    }

    //public final void resetMultiplexer() {
    //    multiplexer = 0;
    //    if (enableMultiplexer)
    //        multiplexer = Math.min(terminalHeight / matrix.length, terminalWidth / matrix[0].length);
    //    if (multiplexer <= 0) multiplexer = 1;
    //    if (CongasCore.isDebug()) logger.info("Multiplexer set to " + multiplexer);
    //    forceUpdate();
    //}

    /**
     * Set output fps and how often render loop processed
     * @param fps frames per second
     */
    public void setFps(int fps) {
        fps = Math.max(0, Math.min(1000, fps));
        this.fps = fps;
        loopTime = fps <= 0 ? 0 : 1000 / fps;
    }

    public void setLiveUpdate(boolean liveUpdate) {
        this.liveUpdate = liveUpdate;
    }

    public int getFps() {
        return fps;
    }

    public int getLoopTime() {
        return loopTime;
    }

    public boolean liveUpdate() {
        return liveUpdate;
    }

    public boolean updateNeeded() {
        return needUpdate;
    }

}
