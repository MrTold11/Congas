package ru.congas.core.application;

import ru.congas.core.annotation.callsuper.CallSuper;

import javax.annotation.Nullable;

/**
 * @author Mr_Told
 */
public abstract class GameActivity extends Activity {

    //Ticks per second
    private int tps = 20;
    private volatile int loopTime = 1000 / tps;

    PauseThread gameThread;

    @Override
    public void onCreate(Bundle args) {
        gameThread = new PauseThread(getName()) {

            @Override
            public void run() {
                try {
                    long loopTimer;
                    while (run) {
                        loopTimer = System.currentTimeMillis();
                        loop();
                        loopTimer = System.currentTimeMillis() - loopTimer;
                        if (loopTimer < loopTime) //noinspection BusyWait
                            sleep(loopTime - loopTimer);

                        while (pause) {
                            synchronized (this) {
                                wait();
                            }
                        }
                    }
                } catch (Exception e) {
                    handleException(e);
                }
            }

            @Override
            protected void handleException(Exception e) {
                throw new RuntimeException(e);
            }

            @Override
            protected void loop() {
                onMainLoop();
            }
        };
        super.onCreate(args);
        gameThread.start();
    }

    @Override
    protected void onResume(@Nullable Bundle extra) {
        super.onResume(extra);
        gameThread.restart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameThread.pause();
    }

    @CallSuper
    protected void onDestroy() {
        gameThread.end();
        super.onDestroy();
    }

    public abstract void onMainLoop();

    protected void setTps(int tps) {
        if (tps <= 0) tps = 1;
        this.tps = tps;
        this.loopTime = 1000 / tps;
    }

    protected int getTps() {
        return tps;
    }

    @Override
    public void onResize(int oldWidth, int oldHeight, int newWidth, int newHeight) {
        screen.getCanvas().clear(newWidth, newHeight);
        screen.updateCanvas();
    }

}
