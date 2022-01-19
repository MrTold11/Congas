package ru.congas;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jline.terminal.Size;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import ru.congas.input.InputThread;
import ru.congas.input.SystemHandler;
import ru.congas.loader.StorageManager;
import ru.congas.output.RenderThread;
import ru.congas.pages.CloseCongas;
import ru.congas.pages.MainMenu;
import ru.congas.pages.Page;
import ru.congas.pages.SettingsPage;

import java.nio.charset.StandardCharsets;
import java.util.Stack;

/**
 * @author Mr_Told
 */
public class CongasClient {

    private static Logger logger = null;

    private static CongasClient instance = null;
    protected static InputThread input = null;
    protected static RenderThread renderer = null;
    private static Terminal terminal = null;
    private static StorageManager storageManager = null;
    private static volatile boolean run = true;
    private static volatile boolean debug = true;
    private static boolean sendReport = false;

    private static final Stack<SimpleGame> pageStack = new Stack<>();
    private static final SystemHandler systemHandler = new SystemHandler();
    private static SimpleGame current = null;

    public CongasClient() {
        instance = this;
    }

    /**
     * Main void: launch logger, terminal, input, output, add shutdown hook
     * @param args ignored
     */
    public static void main(String[] args) {
        new CongasClient();
        try {
            logger = LogManager.getLogger(CongasClient.class);
            logger.info("Starting CongasClient");
            Runtime.getRuntime().addShutdownHook(new Thread(CongasClient::close, "ShutdownHook"));

            input = new InputThread();
            terminal = TerminalBuilder.terminal(input);

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
            renderer = new RenderThread(terminal);

            storageManager.init(true);
            input.addHandler(systemHandler);

            openPage(new MainMenu());

            renderer.start();
            input.start();
        } catch (Exception e) {
            if (logger == null) {
                System.err.println("Error during logger init. This exception won't be reported!");
                e.printStackTrace();
            } else logger.fatal("Failed to start Congas client", e);
            close();
        }
    }

    public synchronized static void openPage(SimpleGame page) {
        if (page == null) {
            logger.warn("Trying to open null page!");
            return;
        }

        for (SimpleGame p : pageStack) {
            if (p.getName().equals(page.getName()) && p.getClass().equals(page.getClass())) {
                while (pageStack.peek() != p) back();
                return;
            }
        }

        if (debug) logger.info("Opening " + page.getName() + " page");
        if (current != null)
            CongasClient.input.removeHandler(current);
        current = page;
        systemHandler.setCurrent(current);
        if (!(page instanceof Page && ((Page) page).isTemporary()))
            pageStack.add(page);
        if (page instanceof SettingsPage) ((SettingsPage) page).initClient(instance);
        CongasClient.renderer.setCanvas(current);
        CongasClient.input.addHandler(current);
    }

    public synchronized static void back() {
        if (pageStack.size() < 2 && pageStack.peek() == current) {
            openPage(new CloseCongas());
            return;
        }
        SimpleGame previous = pageStack.peek() == current ? pageStack.pop() : current;
        if (debug) logger.info("Going back from " + previous.getName() + " to " + pageStack.peek().getName());
        input.removeHandler(previous);
        current = pageStack.peek();
        renderer.setCanvas(current);
        systemHandler.setCurrent(current);
        input.addHandler(current);
    }

    /**
     * Proper way to stop everything
     */
    public static void close() {
        if (!run) return;
        if (logger != null) logger.info("Stopping CongasClient from thread " + Thread.currentThread().getName());

        run = false;
        try {
            if (terminal != null) terminal.close();
            if (storageManager != null) storageManager.close();
        } catch (Exception e) {
            if (logger != null) logger.fatal(e);
        } finally {
            if (logger != null) logger.info("end!");
        }
        System.exit(0);
    }

    public static boolean isRunning() {
        return run;
    }

    public static boolean isDebug() {
        return debug;
    }

    public void enableDebug(boolean enable) {
        logger.info("Debug mode " + (enable ? "ON" : "OFF"));
        debug = enable;
    }

    public static boolean reportSendEnabled() {
        return sendReport;
    }

    public void enableReport(boolean enable) {
        logger.info("Report send " + (enable ? "ON" : "OFF"));
        sendReport = enable;
    }

}
