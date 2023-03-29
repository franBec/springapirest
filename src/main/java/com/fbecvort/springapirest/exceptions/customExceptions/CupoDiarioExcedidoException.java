package com.fbecvort.springapirest.exceptions.customExceptions;

public class CupoDiarioExcedidoException extends RuntimeException {
    public CupoDiarioExcedidoException(String message, Throwable cause) {
        super(message, cause);
    }
}
