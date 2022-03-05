package ru.congas.core.input;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jline.terminal.Attributes;
import org.jline.terminal.Terminal;
import org.jline.utils.NonBlockingReader;
import ru.congas.core.CongasCore;
import ru.congas.core.application.Activity;
import ru.congas.core.input.keys.Key;
import ru.congas.core.input.keys.KeyPressed;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Input handler thread. Waits for incoming key press (char input) and passes it to handlers.
 * @author Mr_Told
 */
public class InputThread extends Thread {

    final Logger logger = LogManager.getLogger(InputThread.class);
    final CongasCore core;
    Terminal terminal;
    NonBlockingReader reader;
    Attributes attrs;

    /**
     * An ordered sequence of InputHandlers for handling input:
     * Lower position means higher priority
     * (so the last handler in the list will be used only if all others didn't handle the input)
     */
    private final List<InputHandler> handlers = Collections.synchronizedList(new LinkedList<>());

    /**
     * Init Input thread
     */
    public InputThread(CongasCore core) {
        super("Input");
        this.core = core;
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
        while (CongasCore.isRunning()) {
            try {
                int read, zeroCounter = 0;

                while (CongasCore.isRunning()) {
                    read = reader.read();
                    if (!CongasCore.isRunning()) break;
                    if (read < 0) {
                        zeroCounter++;
                        if (CongasCore.isDebug() && (zeroCounter & (zeroCounter - 1)) == 0)
                            logger.warn("Got < 0 input while running: " + read + "; Counter = " + zeroCounter);
                        continue;
                    }

                    if (read == Key.ESCAPE.getCode() && reader.peek(1) > 0) {
                        List<Character> sequence = new ArrayList<>();
                        while (reader.peek(1) > 0 && (reader.peek(1) != 27 || sequence.size() == 0)) {
                            int r = reader.read();
                            if (r < 0) break;
                            sequence.add((char) r);
                        }
                        KeyPressed event = readSequence(sequence);
                        if (event != null) {
                            safeHandle(event);
                        } else if (CongasCore.isDebug()) {
                            if (sequence.size() < 10) logger.warn("Unknown sequence: " + sequence);
                            else logger.warn("Unknown sequence: " + sequence.subList(0, 10) + " and " + (sequence.size() - 9) + " more elements");
                        }
                        continue;
                    }

                    safeHandle(new KeyPressed((char) read, false, false, false));
                }

                reader.close();
            } catch (Exception e) {
                core.handleSystemThreadException("Fatal error into Input Thread: ",
                        handlers.size() == 0 ? "null" : handlers.get(handlers.size() - 1).getHandlerName(), e);
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

        if (Key.getExactKey(sequence.get(0)) == Key.ESCAPE) {
            if (sequence.size() == 1)
                //return new KeyPressed(CONTROL, shift, true, alt);
                return new KeyPressed(Key.CONTROL, shift, control, alt);
            if (sequence.size() == 2)
                //return new KeyPressed((char) sequence.get(1).intValue(), shift, true, alt);
                return new KeyPressed(sequence.get(1), shift, control, alt);
            sequence.remove(0);
        } else if (sequence.size() < 2)
            //return new KeyPressed((char) sequence.get(0).intValue(), shift, false, true);
            return new KeyPressed(sequence.get(0), shift, control, alt);

        if (sequence.get(0) == '[') {
            switch (sequence.get(1)) {
                case 'A': return new KeyPressed(Key.UP, shift, control, alt);
                case 'B': return new KeyPressed(Key.DOWN, shift, control, alt);
                case 'C': return new KeyPressed(Key.RIGHT, shift, control, alt);
                case 'D': return new KeyPressed(Key.LEFT, shift, control, alt);
                case '1':
                    if (sequence.get(2) == '~') return new KeyPressed(Key.HOME, shift, control, alt);
                    if (sequence.size() < 4 || sequence.get(3) != '~') return null;
                    switch (sequence.get(2)) {
                        case '1': return new KeyPressed(Key.F1, shift, control, alt);
                        case '2': return new KeyPressed(Key.F2, shift, control, alt);
                        case '3': return new KeyPressed(Key.F3, shift, control, alt);
                        case '4': return new KeyPressed(Key.F4, shift, control, alt);
                        case '5': return new KeyPressed(Key.F5, shift, control, alt);
                        case '7': return new KeyPressed(Key.F6, shift, control, alt);
                        case '8': return new KeyPressed(Key.F7, shift, control, alt);
                        case '9': return new KeyPressed(Key.F8, shift, control, alt);
                    }
                    return null;
                case '2':
                    if (sequence.size() < 4 || sequence.get(3) != '~') return null;
                    switch (sequence.get(2)) {
                        case '0': return new KeyPressed(Key.F9, shift, control, alt);
                        case '1': return new KeyPressed(Key.F10, shift, control, alt);
                        case '3': return new KeyPressed(Key.F11, shift, control, alt);
                        case '4': return new KeyPressed(Key.F12, shift, control, alt);
                    }
                    return null;
                case '4':
                    if (sequence.get(2) == '~') return new KeyPressed(Key.END, shift, control, alt);
                    return null;
                case '5':
                    if (sequence.get(2) == '~') return new KeyPressed(Key.PAGE_UP, shift, control, alt);
                    return null;
                case '6':
                    if (sequence.get(2) == '~') return new KeyPressed(Key.PAGE_DOWN, shift, control, alt);
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
            if (CongasCore.isDebug()) logger.info("Handle pressed char: " + event);
            handle(event);
        } catch (Exception e) {
            core.handleSystemThreadException("Fatal error while handling input: ",
                    handlers.size() == 0 ? "null" : handlers.get(handlers.size() - 1).getHandlerName(), e);
        }
    }

    private void handle(KeyPressed event) {
        if ((event.getSystemKey() == null || event.getSystemKey() == Key.NONE) && CongasCore.isDebug())
            logger.warn("Key press with null key: " + event);

        for (InputHandler h : handlers) {
            //todo optimize
            if (h instanceof Activity) {
                ((Activity) h).queueInput(event);
                break;
            }
            if (h.handle(event)) break;
        }
    }

    /**
     * Handler management methods
     */
    public void addHandler(InputHandler handler) {
        if (CongasCore.isDebug()) logger.info("Handler registered: " + handler.getHandlerName());
        if (handlers.contains(handler)) removeHandler(handler);
        handlers.add(handler);
    }

    public void removeHandler(InputHandler handler) {
        if (CongasCore.isDebug()) logger.info("Handler unregistered: " + handler.getHandlerName());
        handlers.remove(handler);
    }

    public void clearHandlers() {
        handlers.clear();
    }

}
