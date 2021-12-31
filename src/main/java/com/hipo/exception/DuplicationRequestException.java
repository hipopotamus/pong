package com.hipo.exception;

public class DuplicationRequestException extends RuntimeException{

    public DuplicationRequestException() {
        super();
    }

    public DuplicationRequestException(String message) {
        super(message);
    }

    public DuplicationRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicationRequestException(Throwable cause) {
        super(cause);
    }

    protected DuplicationRequestException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
