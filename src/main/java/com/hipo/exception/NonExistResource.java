package com.hipo.exception;

public class NonExistResource extends RuntimeException{

    public NonExistResource() {
    }

    public NonExistResource(String message) {
        super(message);
    }

    public NonExistResource(String message, Throwable cause) {
        super(message, cause);
    }

    public NonExistResource(Throwable cause) {
        super(cause);
    }

    public NonExistResource(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
