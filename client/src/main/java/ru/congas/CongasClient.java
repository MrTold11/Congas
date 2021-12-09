package ru.congas;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jline.terminal.Size;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import ru.congas.input.InputThread;
import ru.congas.input.SystemHandler;
import ru.congas.output.RenderThread;

/**
 * @author Mr_Told
 */
public class CongasClient {

    private static Logger logger = null;

    public static InputThread input = null;
    public static RenderThread renderer = null;
    public static Terminal terminal = null;
    public static volatile boolean run = true;
    public static boolean debug = true;

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

            input = new InputThread();
            renderer = new RenderThread();

            input.addHandler(new SystemHandler());

            //new TestInputOutput().launch();

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

    /**
     * Proper way to stop everything
     */
    public static void close() {
        if (!run) return;
        logger.info("Stopping CongasClient...");

        run = false;
        try {
            if (terminal != null) terminal.close();
        } catch (Exception e) {
            logger.fatal(e);
        } finally {
            logger.info("end!");
        }
        System.exit(0);
    }

}
