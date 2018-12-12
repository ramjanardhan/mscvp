package com.mss.ediscv.util;

public class ServiceLocatorException extends Exception {

    private static final long serialVersionUID = 1L;

    public ServiceLocatorException() {
        super();
    }

    public ServiceLocatorException(String message) {
        super(message);
    }

    public ServiceLocatorException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceLocatorException(Throwable cause) {
        super(cause);
    }
}