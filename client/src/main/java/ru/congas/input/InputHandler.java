package ru.congas.input;

import ru.congas.input.keys.KeyPressed;

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
    @Deprecated
    default boolean handle(int c) {
        return false;
    }

    /**
     * Handles input if previous handlers haven't processed it.
     * Default behaviour - just handle char.
     * Tip: check keys with Keycode class
     * @param event key press event
     * @return true if it was handled (false if not)
     */
    default boolean handle(KeyPressed event) {
        if (event.isCharacter())
            return handle(event.getChar());
        return handle(event.getSystemKey().getCode());
    }

    String getHandlerName();

}
