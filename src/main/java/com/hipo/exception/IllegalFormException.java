package com.hipo.exception;

import org.springframework.validation.Errors;

public class IllegalFormException extends RuntimeException{

    private Errors errors;

    public IllegalFormException(Errors errors) {
        super();
        this.errors = errors;
    }

    public IllegalFormException() {
        super();
    }

    public IllegalFormException(String message) {
        super(message);
    }

    public IllegalFormException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalFormException(Throwable cause) {
        super(cause);
    }

    protected IllegalFormException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
