package ru.congas.core.input;

import ru.congas.core.input.keys.KeyPressed;

/**
 * @author Mr_Told
 */
public interface InputHandler {

    /**
     * Handles input if previous handlers haven't processed it.
     * Default behaviour - just handle char.
     * Tip: check keys with Keycode class
     * @param event key press event
     * @return true if it was handled (false if not)
     */
    default boolean handle(KeyPressed event) {
        return false;
    }

    String getHandlerName();

}
