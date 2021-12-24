package com.hipo.exception;

public class NonExistResourceException extends RuntimeException{

    public NonExistResourceException() {
    }

    public NonExistResourceException(String message) {
        super(message);
    }

    public NonExistResourceException(String message, Throwable cause) {
        super(message, cause);
    }

    public NonExistResourceException(Throwable cause) {
        super(cause);
    }

    public NonExistResourceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
