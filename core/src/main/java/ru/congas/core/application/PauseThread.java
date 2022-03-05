package ru.congas.core.application;

/**
 * @author Mr_Told
 */
public abstract class PauseThread extends Thread {

    protected volatile boolean run = true;
    protected volatile boolean pause = false;

    public PauseThread(String name) {
        super(name);
    }

    @Override
    public void run() {
        try {
            while (run) {
                loop();

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

    protected abstract void handleException(Exception e);

    protected abstract void loop();

    public void pause() {
        pause = true;
    }

    public void restart() {
        pause = false;
        synchronized (this) {
            notify();
        }
    }

    public void end() {
        run = false;
        restart();
    }

}
