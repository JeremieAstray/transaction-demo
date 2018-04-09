package com.jeremie.exception;

/**
 */
public class ClassNotDeclearException extends Exception {

    public ClassNotDeclearException() {
    }

    public ClassNotDeclearException(String message) {
        super(message);
    }

    public ClassNotDeclearException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClassNotDeclearException(Throwable cause) {
        super(cause);
    }

    public ClassNotDeclearException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
