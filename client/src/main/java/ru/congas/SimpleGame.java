package ru.congas;

import ru.congas.input.InputHandler;
import ru.congas.output.Canvas;

/**
 * @author Mr_Told
 */
public abstract class SimpleGame extends Canvas implements InputHandler {

    /**
     * Default escape behavior is to pause a game (with default pause) or go to the previous page (if it's not a game).
     * If you wish to disable default behaviour, set this to true (double-clicked escape will still close everything).
     */
    private boolean overrideEscape = false;
    /**
     * Whether this class should be used as standalone game or (if false) as other class (not defined as a game)
     */
    private boolean isGame = true;

    /**
     * @deprecated As it has enableMultiplexer. This value is ignored, for enabling multiplexer, call
     * enableMultiplexer() void.
     * @param name Name
     * @param enableMultiplexer deprecated
     * @param eraseScreen erase screen before new frame render
     * @param resetMatrix reset matrices before it's update
     * @param liveUpdate update on every frame
     * @param fps max frames per second
     * @param matrix_w init matrices width
     * @param matrix_h init matrices height
     */
    @Deprecated
    public SimpleGame(String name, boolean enableMultiplexer, boolean eraseScreen,
                      boolean resetMatrix, boolean liveUpdate, int fps, int matrix_w, int matrix_h) {
        super(name, resetMatrix, liveUpdate, fps, matrix_w, matrix_h);
    }

    /**
     * @deprecated EraseScreen parameter have no effect anymore as it doesn't work
     * @param name Name
     * @param eraseScreen erase screen before new frame render
     * @param resetMatrix reset matrices before it's update
     * @param liveUpdate update on every frame
     * @param fps max frames per second
     * @param matrix_w init matrices width
     * @param matrix_h init matrices height
     */
    @Deprecated
    public SimpleGame(String name, boolean eraseScreen, boolean resetMatrix,
                      boolean liveUpdate, int fps, int matrix_w, int matrix_h) {
        super(name, resetMatrix, liveUpdate, fps, matrix_w, matrix_h);
    }

    /**
     * @param name Name
     * @param resetMatrix reset matrices before it's update
     * @param liveUpdate update on every frame
     * @param fps max frames per second
     * @param matrix_w init matrices width
     * @param matrix_h init matrices height
     */
    public SimpleGame(String name, boolean resetMatrix, boolean liveUpdate,
                      int fps, int matrix_w, int matrix_h) {
        super(name, resetMatrix, liveUpdate, fps, matrix_w, matrix_h);
    }

    public String getHandlerName() {
        return getName() + "_handler";
    }

    protected final void exit() {
        CongasClient.back();
    }

    protected final void exit(SimpleGame nextPage) {
        if (nextPage == null) {
            logger.warn("Couldn't exit to new page from " + getName() + ": got null page");
            exit();
            return;
        }
        CongasClient.back();
        CongasClient.openPage(nextPage);
    }

    protected void setOverrideEscape(boolean overrideEscape) {
        this.overrideEscape = overrideEscape;
    }

    public boolean overrideEscape() {
        return overrideEscape;
    }

    public void setGameStatus(boolean isGame) {
        this.isGame = isGame;
    }

    public boolean isGame() {
        return isGame;
    }

}
