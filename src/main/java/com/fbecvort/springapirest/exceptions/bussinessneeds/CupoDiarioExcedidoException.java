package com.fbecvort.springapirest.exceptions.bussinessneeds;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.math.BigDecimal;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CupoDiarioExcedidoException extends RuntimeException {

    private Long id;
    private BigDecimal montoDiarioAcumulado;
    private BigDecimal montoARetirar;

    public CupoDiarioExcedidoException(){
        super();
    }

    public CupoDiarioExcedidoException(Long id, BigDecimal montoDiarioAcumulado, BigDecimal montoARetirar) {
        super("Cupo diario excedido");
        this.id = id;
        this.montoDiarioAcumulado = montoDiarioAcumulado;
        this.montoARetirar = montoARetirar;
    }
}
