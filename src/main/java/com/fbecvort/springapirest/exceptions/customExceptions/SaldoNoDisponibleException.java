package com.fbecvort.springapirest.exceptions.customExceptions;

public class SaldoNoDisponibleException extends RuntimeException {
    public SaldoNoDisponibleException(String message, Throwable cause) {
        super(message, cause);
    }
}
