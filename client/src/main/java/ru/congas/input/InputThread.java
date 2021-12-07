package ru.congas.input;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jline.terminal.Attributes;
import org.jline.terminal.Terminal;
import org.jline.utils.NonBlockingReader;
import ru.congas.CongasClient;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Mr_Told
 */
public class InputThread extends Thread {

    final Logger logger;
    final Terminal terminal;
    final NonBlockingReader reader;
    final Attributes attrs;

    /**
     * An ordered sequence of InputHandlers for handling input:
     * Lower position means higher priority
     * (so the last handler in the list will be used only if all others didn't handle the input)
     */
    private List<InputHandler> handlers = new LinkedList<>();

    /**
     * Init Input thread
     */
    public InputThread() {
        logger = LogManager.getLogger(InputThread.class);
        terminal = CongasClient.terminal;

        attrs = terminal.enterRawMode();
        reader = terminal.reader();

        logger.info("Input loaded.");
    }

    /**
     * Loop for input reading and further processing
     */
    public void run() {
        try {
            int read;
            while (CongasClient.run) {
                read = reader.read();
                if (CongasClient.debug)
                    logger.info("Pressed char: " + Keycode.getKeyName(read) + " (" + read + ")");
                handle(read);
            }
            reader.close();
        } catch (IOException e) {
            logger.fatal(e);
        } finally {
            terminal.setAttributes(attrs);
        }
    }

    /**
     * Handle user input with handlers
     * @param c key code
     */
    private void handle(int c) {
        for (InputHandler h : handlers)
            if (h.handle(c)) break;
    }

    /**
     * Handler management methods
     */
    public void setHandlers(LinkedList<InputHandler> handlers) {
        this.handlers = handlers;
    }

    public void addHandler(InputHandler handler) {
        handlers.add(handler);
    }

    public void removeHandler(InputHandler handler) {
        handlers.remove(handler);
    }

}
