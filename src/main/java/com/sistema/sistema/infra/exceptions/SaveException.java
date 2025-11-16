package com.sistema.sistema.infra.exceptions;

public class SaveException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public SaveException(String message, Throwable cause) {
        super(message, cause);
    }

    public SaveException(String message) {
        super(message);
    }

}
