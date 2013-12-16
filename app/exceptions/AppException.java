package exceptions;

/**
 * Created by nepjua on 12/15/13.
 */
public class AppException extends Exception {
    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public AppException(String message) {
        super(message);
    }
}
