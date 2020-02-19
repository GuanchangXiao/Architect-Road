package com.rabbit.component.api.exception;

/**
 * Created by perl on 2020-02-19.
 */
public class MessageException extends Exception {
    private static final long serialVersionUID = -2281812956991184300L;

    public MessageException() {
        super();
    }

    public MessageException(String message) {
        super(message);
    }

    public MessageException(Throwable cause) {
        super(cause);
    }

    public MessageException(String message, Throwable cause) {
        super(message, cause);
    }
}
