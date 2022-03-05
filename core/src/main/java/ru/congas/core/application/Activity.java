package ru.congas.core.application;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.congas.core.CongasCore;
import ru.congas.core.annotation.callsuper.CallSuper;
import ru.congas.core.exception.CalledFromWrongThreadException;
import ru.congas.core.input.InputHandler;
import ru.congas.core.input.keys.KeyPressed;
import ru.congas.core.output.canvas.Canvas;
import ru.congas.core.output.ScreenBuffer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Mr_Told
 */
public abstract class Activity implements InputHandler {

    protected final Logger logger = LogManager.getLogger(this.getClass());
    private CongasCore core = null;

    private Activity parent = null;
    private final UiThread uiThread;
    protected final ScreenBuffer screen;

    private final String name;

    public interface ActivityInstanceCreateRunnable {
        void run(Activity activity);
    }

    public static Activity createInstance(Class<? extends Activity> clazz, CongasCore core, Bundle args, ActivityInstanceCreateRunnable runnable) {
        Activity next;
        try {
            next = clazz.getDeclaredConstructor().newInstance();
            runnable.run(next);
            next.init(null, core, args);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return next;
    }

    public Activity() {
        Class<?> enclosingClass = getClass().getEnclosingClass();
        this.name = enclosingClass != null ? enclosingClass.getSimpleName() : getClass().getSimpleName();

        screen = new ScreenBuffer(this);
        uiThread = new UiThread(this);
    }

    @Override
    public String getHandlerName() {
        return getName() + "_handler";
    }

    protected final void openActivity(@Nonnull Class<? extends Activity> activity, @Nullable Bundle args, boolean closeCurrent) {
        checkThread();

        Activity parent = closeCurrent ? this.parent : this;
        try {
            Activity next = activity.getDeclaredConstructor().newInstance();

            onPause();
            core.switchActivity(this, next);
            try {
                next.init(parent, core, args);
            } catch (Exception e) {
                logger.warn("Error while trying to init new application: ", e);
                core.switchActivity(next, this);
                onResume(null);
                return;
            }

            if (closeCurrent)
                onDestroy();
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    protected final void exceptionClose() {
        try {
            if (parent != null) {
                core.switchActivity(this, parent);
                parent.onResume(null);
            } else
                CongasCore.close(this);
        } catch (Exception e1) {
            core.handleSystemThreadException("Exception while trying to close application with exception", name, e1);
        }
    }

    public final void closeActivity() {
        closeActivity(null);
    }

    protected final void closeActivity(@Nullable Bundle result) {
        checkThread();

        onPause();
        if (parent != null) {
            core.switchActivity(this, parent);
            parent.onResume(result);
        }
        onDestroy();

        if (parent == null) CongasCore.close(this);
    }

    private void init(@Nullable Activity parent, @Nonnull CongasCore core, @Nullable Bundle args) {
        this.core = core;
        this.parent = parent;
        this.uiThread.start();
        runOnUiThread(() -> {
            onCreate(args);
            onResume(null);
        });
    }

    @CallSuper
    protected void onCreate(@Nullable Bundle args) {
        logger.info("onCreate " + name);
        if (core == null)
            throw new RuntimeException("Activity was not initialized correctly!");
    }

    @CallSuper
    protected void onResume(@Nullable Bundle extra) {
        logger.info("onResume " + name);
        uiThread.restart();
    }

    @CallSuper
    protected void onPause() {
        logger.info("onPause " + name);
        uiThread.pause();
    }

    @CallSuper
    protected void onDestroy() {
        uiThread.end();
    }

    public void onResize(int oldWidth, int oldHeight, int newWidth, int newHeight) {}

    protected Canvas getCanvas() {
        return screen.getCanvas();
    }

    public ScreenBuffer getScreen() {
        return screen;
    }

    public final void checkThread() {
        CalledFromWrongThreadException.checkThread(uiThread);
    }

    public void runOnUiThread(Runnable runnable) {
        uiThread.runOnUiThread(runnable);
    }

    public final void queueInput(KeyPressed event) {
        uiThread.queueInput(event);
    }

    public Logger getLogger() {
        return logger;
    }

    public String getName() {
        return name;
    }

}
