package ru.congas;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import ru.congas.input.InputThread;

/**
 * @author Mr_Told
 */
public class CongasClient {

    public static Logger logger = null;

    public static InputThread input = null;
    public static Terminal terminal = null;
    public static boolean run = true;
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
            input = new InputThread();
            input.start();

        } catch (Exception e) {
            if (logger == null) {
                System.err.println("Error during logger init");
                e.printStackTrace();
            } else
                logger.fatal("Failed to start Congas server", e);
        }
    }

    /**
     * proper way to stop everything
     */
    public static void close() {
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
