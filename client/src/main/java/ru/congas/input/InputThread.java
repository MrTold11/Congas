package ru.congas.input;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jline.terminal.Attributes;
import org.jline.terminal.Terminal;
import org.jline.utils.NonBlockingReader;
import ru.congas.CongasClient;
import ru.congas.pages.ErrorScreen;

import java.util.LinkedList;
import java.util.List;

/**
 * Input handler thread. Waits for incoming key press (char input) and passes it to handlers.
 * @author Mr_Told
 */
public class InputThread extends Thread {

    final Logger logger = LogManager.getLogger(InputThread.class);
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
    public InputThread(Terminal terminal) {
        super("Input");
        this.terminal = terminal;

        attrs = terminal.enterRawMode();
        reader = terminal.reader();

        logger.info("Input loaded.");
    }

    /**
     * Loop for input reading and further processing
     */
    public void run() {
        while (CongasClient.isRunning()) {
            try {
                int read;
                while (CongasClient.isRunning()) {
                    read = reader.read();
                    if (CongasClient.isDebug())
                        logger.info("Pressed char: " + Keycode.getKeyName(read) + " (" + read + ")");
                    handle(read);
                }
                reader.close();
            } catch (Exception e) {
                logger.fatal("Fatal error into Input Thread: ", e);
                CongasClient.openPage(new ErrorScreen("Fatal error into Input Thread ",
                        handlers.size() == 0 ? "null" : handlers.get(handlers.size() - 1).getHandlerName(), e));
            }
        }
        terminal.setAttributes(attrs);
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
        if (CongasClient.isDebug()) {
            StringBuilder sb = new StringBuilder();
            for (InputHandler ih : handlers)
                sb.append(", ").append(ih.getHandlerName());
            logger.info("Handler list updated: " + (handlers.size() == 0 ? "null" : sb.substring(2)));
        }
        this.handlers = handlers;
    }

    public void addHandler(InputHandler handler) {
        if (CongasClient.isDebug()) logger.info("Handler registered: " + handler.getHandlerName());
        if (handlers.contains(handler)) removeHandler(handler);
        handlers.add(handler);
    }

    public void removeHandler(InputHandler handler) {
        if (CongasClient.isDebug()) logger.info("Handler unregistered: " + handler.getHandlerName());
        handlers.remove(handler);
    }

}
