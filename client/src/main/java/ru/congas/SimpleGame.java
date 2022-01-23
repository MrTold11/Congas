package ru.congas;

/**
 * @author Mr_Told
 */
public abstract class SimpleGame extends SimpleApp {

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
        this(name, resetMatrix, liveUpdate, fps, matrix_w, matrix_h);
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
        setGameStatus(true);
    }

}
