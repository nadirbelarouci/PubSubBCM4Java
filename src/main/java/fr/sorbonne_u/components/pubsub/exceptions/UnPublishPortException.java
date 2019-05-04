package fr.sorbonne_u.components.pubsub.exceptions;

public class UnPublishPortException extends RuntimeException {
    public UnPublishPortException() {
        super();
    }

    public UnPublishPortException(String message) {
        super(message);
    }

    public UnPublishPortException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnPublishPortException(Throwable cause) {
        super(cause);
    }


}
