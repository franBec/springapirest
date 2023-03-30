package com.fbecvort.springapirest.exception.bussinessneed;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.math.BigDecimal;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SaldoNoDisponibleException extends RuntimeException {

    private Long id;
    private BigDecimal saldo;
    private BigDecimal monto;

    public SaldoNoDisponibleException(){
        super();
    }

    public SaldoNoDisponibleException(Long id, BigDecimal saldo, BigDecimal monto) {
        super("Saldo no disponible");
        this.id = id;
        this.saldo = saldo;
        this.monto = monto;
    }
}
