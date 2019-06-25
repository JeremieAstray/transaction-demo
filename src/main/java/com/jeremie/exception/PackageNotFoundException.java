package com.jeremie.exception;

/**
 */
public class PackageNotFoundException extends Exception {

    public PackageNotFoundException() {
    }

    public PackageNotFoundException(String message) {
        super(message);
    }

    public PackageNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public PackageNotFoundException(Throwable cause) {
        super(cause);
    }

    public PackageNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
