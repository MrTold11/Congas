package ru.congas.input;

/**
 * @author Mr_Told
 */
public interface InputHandler {

    /**
     * Handles input if previous handlers haven't processed it.
     * Tip: check keys with Keycode class
     * @param c key code
     * @return true if it was handled (false if not)
     */
    boolean handle(int c);

    String getHandlerName();

}
