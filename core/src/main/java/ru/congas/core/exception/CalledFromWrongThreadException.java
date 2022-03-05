package ru.congas.core.exception;

/**
 * @author Mr_Told
 */
public class CalledFromWrongThreadException extends RuntimeException {

    public CalledFromWrongThreadException(Thread wrongThread) {
        super("This operation cannot be performed by a background thread: " + wrongThread.getName());
    }

    public static void checkThread(Thread thread) {
        if (thread != Thread.currentThread()) throw new CalledFromWrongThreadException(Thread.currentThread());
    }

}
