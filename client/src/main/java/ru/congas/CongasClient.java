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
import ru.congas.pages.MainMenu;
import ru.congas.pages.Page;

import java.util.Stack;

/**
 * @author Mr_Told
 */
public class CongasClient {

    private static Logger logger = null;

    protected static InputThread input = null;
    protected static RenderThread renderer = null;
    private static Terminal terminal = null;
    private static StorageManager storageManager = null;
    private static volatile boolean run = true;
    private static volatile boolean debug = true;

    private static final Stack<SimpleGame> pageStack = new Stack<>();
    private static final SystemHandler systemHandler = new SystemHandler();
    private static SimpleGame current = null;

    /**
     * Main void: launch logger, terminal, input, output, add shutdown hook
     * @param args ignored
     */
    public static void main(String[] args) {
        try {
            logger = LogManager.getLogger(CongasClient.class);
            logger.info("Starting CongasClient");
            Runtime.getRuntime().addShutdownHook(new Thread(CongasClient::close, "ShutdownHook"));

            terminal = TerminalBuilder.builder().jansi(true).build();
            if (terminal == null) throw new RuntimeException("Failed to create terminal");
            Size s = terminal.getSize();
            if (s.getRows() <= 1 || s.getColumns() <= 1) {
                logger.error("Too small terminal: " + s.getColumns() + "x" + s.getRows());
                terminal.output().write("This terminal is not suitable for Congas. Sorry :(".getBytes());
                Thread.sleep(7000);
                close();
                return;
            }

            storageManager = new StorageManager();
            input = new InputThread(terminal);
            renderer = new RenderThread(terminal);

            storageManager.init(true);
            input.addHandler(systemHandler);

            //new TestInputOutput().launch();
            //new TestPictureOutput().launch();
            openPage(new MainMenu());

            renderer.start();
            input.start();
        } catch (Exception e) {
            if (logger == null) {
                System.err.println("Error during logger init");
                e.printStackTrace();
            } else
                logger.fatal("Failed to start Congas client", e);
        }
    }

    public synchronized static void openPage(SimpleGame page) {
        for (SimpleGame p : pageStack) {
            if (p.getName().equals(page.getName()) && p.getClass().equals(page.getClass())) {
                while (pageStack.peek() != p) back();
                return;
            }
        }

        if (debug) logger.info("Opening " + page.getName() + " page");
        if (current != null)
            CongasClient.input.removeHandler(current);
        CongasClient.renderer.setCanvas(page);
        current = page;
        systemHandler.setCurrent(page);
        CongasClient.input.addHandler(page);
        if (page instanceof Page && ((Page) page).isTemporary())
            return;
        pageStack.add(page);
    }

    public synchronized static void back() {
        if (pageStack.size() < 2) return;
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
        logger.info("Stopping CongasClient...");

        run = false;
        try {
            if (terminal != null) terminal.close();
            if (storageManager != null) storageManager.close();
        } catch (Exception e) {
            logger.fatal(e);
        } finally {
            logger.info("end!");
        }
        System.exit(0);
    }

    public static boolean isRunning() {
        return run;
    }

    public static boolean isDebug() {
        return debug;
    }

}
