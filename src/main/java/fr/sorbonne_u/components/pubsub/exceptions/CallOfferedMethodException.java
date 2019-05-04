package fr.sorbonne_u.components.pubsub.exceptions;

public class CallOfferedMethodException extends RuntimeException {
    public CallOfferedMethodException() {
        super();
    }

    public CallOfferedMethodException(String message) {
        super(message);
    }

    public CallOfferedMethodException(String message, Throwable cause) {
        super(message, cause);
    }

    public CallOfferedMethodException(Throwable cause) {
        super(cause);
    }
}
