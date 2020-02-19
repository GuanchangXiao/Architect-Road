package com.rabbit.component.api.exception;

/**
 * Created by perl on 2020-02-19.
 */
public class MessageRuntimeException extends RuntimeException{
    private static final long serialVersionUID = -6996117249208700808L;

    public MessageRuntimeException() {
        super();
    }

    public MessageRuntimeException(String message) {
        super(message);
    }

    public MessageRuntimeException(Throwable cause) {
        super(cause);
    }

    public MessageRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
