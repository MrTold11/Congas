package ru.congas.core.application;

import ru.congas.core.input.keys.KeyPressed;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Mr_Told
 */
public class UiThread extends SuspendedThread {

    private final Activity activity;

    List<Runnable> tasksQueue = Collections.synchronizedList(new LinkedList<>());
    List<KeyPressed> inputQueue = Collections.synchronizedList(new LinkedList<>());

    public UiThread(@Nonnull Activity activity) {
        super(activity.getName());
        this.activity = activity;
    }

    @Override
    protected void handleException(Exception e) {
        activity.getLogger().error("Fatal error in " + activity.getName() + " application: ", e);
        if (!pause && run)
            activity.exceptionClose();
    }

    @Override
    protected void loop() {
        if (!inputQueue.isEmpty()) {
            KeyPressed event = inputQueue.remove(0);
            if (event != null)
                activity.handle(event);
        }

        if (!tasksQueue.isEmpty()) {
            Runnable task = tasksQueue.remove(0);
            if (task != null)
                task.run();
        }

        //process animations
        try {
            Thread.sleep(1);
        } catch (InterruptedException ignored) {}
    }

    public void queueInput(KeyPressed key) {
        inputQueue.add(key);
    }

    public void runOnUiThread(Runnable runnable) {
        tasksQueue.add(runnable);
    }

}
