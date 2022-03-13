package ru.congas.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jline.terminal.Size;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.terminal.impl.jansi.win.JansiWinSysTerminal;
import ru.congas.core.application.Activity;
import ru.congas.core.application.Bundle;
import ru.congas.core.input.InputHandler;
import ru.congas.core.input.InputThread;
import ru.congas.core.loader.LauncherLoader;
import ru.congas.core.loader.StorageManager;
import ru.congas.core.output.RenderThread;
import ru.congas.core.pages.ErrorActivity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Mr_Told
 */
public class CongasCore {

    protected static Logger logger = null;
    final String name = "CongasCore";

    protected static CongasCore instance = null;
    protected static InputThread input = null;
    protected static RenderThread renderer = null;
    protected static Terminal terminal = null;
    protected static StorageManager storageManager = null;

    protected static volatile boolean run = true;
    protected static volatile boolean debug = true;
    protected static boolean sendReport = false;

    protected volatile static Activity current = null;

    public CongasCore() throws Exception {
        if (instance != null) throw new Exception("Congas core is already running");

        instance = this;

        try {
            logger.info("Starting " + name);
            Runtime.getRuntime().addShutdownHook(new Thread(() -> instance.close(), "ShutdownHook"));

            input = new InputThread(this);
            terminal = TerminalBuilder.terminal(input);
            setTitle("Congas");

            Size s = terminal.getSize();
            if (s.getRows() <= 1 || s.getColumns() <= 1) {
                logger.error("Too small terminal: " + s.getColumns() + "x" + s.getRows());
                terminal.writer().print("This terminal is not suitable for Congas. Sorry :(");
                Thread.sleep(10000);
                close();
                return;
            }

            storageManager = new StorageManager();

            input.init(terminal);
            renderer = new RenderThread(instance, terminal);

            storageManager.init(true);

            if (this.getClass() == CongasCore.class) {

            }
            //openActivity(ErrorActivity.class, null);
            //handleSystemThreadException("exception_lol", "me", null);

            renderer.start();
            input.start();
        } catch (Exception e) {
            if (logger == null) {
                System.err.println("Error during logger init. This exception won't be reported!");
                e.printStackTrace();
            } else
                logger.fatal("Failed to start Congas Core", e);
            close();
        }
    }

    public static void main(String[] args) throws Exception {
        logger = LogManager.getLogger(CongasCore.class);
        Class<? extends CongasCore> launcherClass = new LauncherLoader().findLoader();
        if (launcherClass == null) {
            new CongasCore();
            return;
        }

        try {
            launcherClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            logger.warn("Exception during loader init: ", e);
            new CongasCore();
        }
    }

    protected void setTitle(String title) {
        if (!run || terminal == null) return;
        terminal.writer().print((char) 27 + "]2;" + title + (char) 7);
    }

    public void handleSystemThreadException(String exception, String cause, Exception e) {
        logger.fatal(exception, e);
        openActivity(ErrorActivity.class, new Bundle()
                        .addExtra("err", exception)
                        .addExtra("cause", cause)
                        .addExtra("exception", e), null);
    }

    protected void openActivity(@Nonnull Class<? extends Activity> clazz, @Nullable Bundle args, @Nullable InputHandler sys) {
        current = Activity.createInstance(clazz, this, args, activity1 -> {
            input.clearHandlers();
            if (sys != null)
                input.addHandler(sys);
            renderer.setScreen(activity1.getScreen());
            input.addHandler(activity1);
        });
    }

    public void switchActivity(@Nonnull Activity parent, @Nonnull Activity child) {
        parent.checkThread();
        if (parent != current)
            throw new RuntimeException("Parent activity is not the current activity!");

        if (debug) logger.info("Activity switched to: " + child.getName());

        input.removeHandler(parent);
        renderer.setScreen(child.getScreen());
        input.addHandler(child);
        current = child;
    }

    /**
     * Proper way to stop everything
     */
    private void close() {
        if (!run) return;
        if (logger != null) logger.info("Stopping core from thread " + Thread.currentThread().getName());

        run = false;
        try {
            if (terminal != null) terminal.close();
            if (storageManager != null) storageManager.close();
        } catch (Exception e) {
            if (logger != null) logger.fatal(e);
        } finally {
            if (logger != null) logger.info("Bye!");
        }
        System.exit(0);
    }

    public static void close(Object caller) {
        if (caller == null) return;

        //todo check permission

        instance.close();
    }

    public static CongasCore getInstance(Activity activity) {
        //todo check permission
        return instance;
    }

    public static boolean isRunning() {
        return run;
    }

    public static boolean isDebug() {
        return debug;
    }

    public static boolean reportSendEnabled() {
        return sendReport;
    }

    public void enableDebug(boolean enable) {
        logger.info("Debug mode " + (enable ? "ON" : "OFF"));
        debug = enable;
    }

    public void enableReport(boolean enable) {
        logger.info("Report send " + (enable ? "ON" : "OFF"));
        sendReport = enable;
    }

}
