package ru.congas.input;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jline.terminal.Attributes;
import org.jline.terminal.Terminal;
import org.jline.utils.NonBlockingReader;
import ru.congas.CongasClient;
import ru.congas.input.keys.Key;
import ru.congas.input.keys.KeyPressed;
import ru.congas.pages.ErrorScreen;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static ru.congas.input.keys.Key.*;

/**
 * Input handler thread. Waits for incoming key press (char input) and passes it to handlers.
 * @author Mr_Told
 */
public class InputThread extends Thread {

    final Logger logger = LogManager.getLogger(InputThread.class);
    Terminal terminal;
    NonBlockingReader reader;
    Attributes attrs;

    /**
     * An ordered sequence of InputHandlers for handling input:
     * Lower position means higher priority
     * (so the last handler in the list will be used only if all others didn't handle the input)
     */
    private final List<InputHandler> handlers = new LinkedList<>();

    /**
     * Init Input thread
     */
    public InputThread() {
        super("Input");
    }

    public void init(Terminal terminal) {
        this.terminal = terminal;

        attrs = terminal.enterRawMode();
        reader = terminal.reader();
        logger.info("Input initialized");
    }

    /**
     * Loop for input reading and further processing. Note that windows input events handle directly into {@link #safeHandle(KeyPressed)}
     * @see org.jline.terminal.impl.AbstractWindowsTerminal#processKeyEvent(boolean, short, char, int)
     */
    public void run() {
        while (CongasClient.isRunning()) {
            try {
                int read, zeroCounter = 0;

                while (CongasClient.isRunning()) {
                    read = reader.read();
                    if (!CongasClient.isRunning()) break;
                    if (read < 0) {
                        zeroCounter++;
                        if (CongasClient.isDebug() && (zeroCounter & (zeroCounter - 1)) == 0)
                            logger.warn("Got < 0 input while running: " + read + "; Counter = " + zeroCounter);
                        continue;
                    }

                    //if (CongasClient.isDebug()) logger.info("Input char: " + read + " (" + (char) read + ")");

                    /*
                    if (read == ESCAPE.getCode() && reader.peek(1) > 0) {
                        List<Integer> sequence = new ArrayList<>();
                        while (reader.peek(1) > 0) {
                            int r = reader.read();
                            if (r < 0) break;
                            sequence.add(r);
                        }
                    if (read == ESCAPE.getCode() && reader.ready()) {
                        List<Integer> sequence = new ArrayList<>();
                        while (reader.ready()) {
                            int r = reader.read();
                            if (r < 0) break;
                            sequence.add(r);
                        }
                    */
                    if (read == ESCAPE.getCode() && reader.peek(1) > 0) {
                        List<Character> sequence = new ArrayList<>();
                        while (reader.peek(1) > 0 && (reader.peek(1) != 27 || sequence.size() == 0)) {
                            int r = reader.read();
                            if (r < 0) break;
                            sequence.add((char) r);
                        }
                        KeyPressed event = readSequence(sequence);
                        if (event != null) {
                            safeHandle(event);
                        } else if (CongasClient.isDebug()) {
                            if (sequence.size() < 10) logger.warn("Unknown sequence: " + sequence);
                            else logger.warn("Unknown sequence: " + sequence.subList(0, 10) + " and " + (sequence.size() - 9) + " more elements");
                        }
                        continue;
                    }

                    //if (CongasClient.isDebug())
                        //logger.info("Pressed char: " + Keycode.getKeyName(read, alt, shift, control) + " (" + read + ")");
                    //handle(read, alt, shift, control);
                    safeHandle(new KeyPressed((char) read, false, false, false));
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
     * Escape sequences solver. Made for linux using putty ssh, not tested with any other platform/protocol.
     * Shift/alt/control data not supported (always false, except for shift if letter is upper case)
     * @param sequence list of keys
     * @return key press event or null if wrong sequence
     */
    private KeyPressed readSequence(List<Character> sequence) {
        if (sequence == null || sequence.size() == 0 || sequence.size() > 10) return null;
        boolean alt = false, shift = false, control = false; //control = Key.getExactKey((char) sequence.get(0).intValue()) == ESCAPE;

        if (Key.getExactKey(sequence.get(0)) == ESCAPE) {
            if (sequence.size() == 1)
                //return new KeyPressed(CONTROL, shift, true, alt);
                return new KeyPressed(CONTROL, shift, control, alt);
            if (sequence.size() == 2)
                //return new KeyPressed((char) sequence.get(1).intValue(), shift, true, alt);
                return new KeyPressed(sequence.get(1), shift, control, alt);
            sequence.remove(0);
        } else if (sequence.size() < 2)
            //return new KeyPressed((char) sequence.get(0).intValue(), shift, false, true);
            return new KeyPressed(sequence.get(0), shift, control, alt);

        if (sequence.get(0) == '[') {
            switch (sequence.get(1)) {
                case 'A': return new KeyPressed(UP, shift, control, alt);
                case 'B': return new KeyPressed(DOWN, shift, control, alt);
                case 'C': return new KeyPressed(RIGHT, shift, control, alt);
                case 'D': return new KeyPressed(LEFT, shift, control, alt);
                case '1':
                    if (sequence.get(2) == '~') return new KeyPressed(HOME, shift, control, alt);
                    if (sequence.size() < 4 || sequence.get(3) != '~') return null;
                    switch (sequence.get(2)) {
                        case '1': return new KeyPressed(F1, shift, control, alt);
                        case '2': return new KeyPressed(F2, shift, control, alt);
                        case '3': return new KeyPressed(F3, shift, control, alt);
                        case '4': return new KeyPressed(F4, shift, control, alt);
                        case '5': return new KeyPressed(F5, shift, control, alt);
                        case '7': return new KeyPressed(F6, shift, control, alt);
                        case '8': return new KeyPressed(F7, shift, control, alt);
                        case '9': return new KeyPressed(F8, shift, control, alt);
                    }
                    return null;
                case '2':
                    if (sequence.size() < 4 || sequence.get(3) != '~') return null;
                    switch (sequence.get(2)) {
                        case '0': return new KeyPressed(F9, shift, control, alt);
                        case '1': return new KeyPressed(F10, shift, control, alt);
                        case '3': return new KeyPressed(F11, shift, control, alt);
                        case '4': return new KeyPressed(F12, shift, control, alt);
                    }
                    return null;
                case '4':
                    if (sequence.get(2) == '~') return new KeyPressed(END, shift, control, alt);
                    return null;
                case '5':
                    if (sequence.get(2) == '~') return new KeyPressed(PAGE_UP, shift, control, alt);
                    return null;
                case '6':
                    if (sequence.get(2) == '~') return new KeyPressed(PAGE_DOWN, shift, control, alt);
                    return null;
            }
            return null;
        }
        return new KeyPressed(sequence.get(0), shift, control, alt);
    }

    /**
     * Handle key press event
     * @param event event data
     */
    public void safeHandle(KeyPressed event) {
        if (event == null) return;
        try {
            if (CongasClient.isDebug()) logger.info("Handle pressed char: " + event);
            handle(event);
        } catch (Exception e) {
            logger.fatal("Fatal error while handling input: ", e);
            CongasClient.openPage(new ErrorScreen("Fatal error while handling input ",
                    handlers.size() == 0 ? "null" : handlers.get(handlers.size() - 1).getHandlerName(), e));
        }
    }

    private void handle(KeyPressed event) {
        if ((event.getSystemKey() == null || event.getSystemKey() == Key.NONE) && CongasClient.isDebug())
            logger.warn("Key press with null key: " + event);

        for (InputHandler h : handlers)
            if (h.handle(event)) break;

    }

    /**
     * Handler management methods
     */
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
