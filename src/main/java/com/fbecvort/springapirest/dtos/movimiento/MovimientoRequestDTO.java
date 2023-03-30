package com.fbecvort.springapirest.dtos.movimiento;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fbecvort.springapirest.enums.TipoMovimiento;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class MovimientoRequestDTO {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    @Builder.Default
    private Date fecha = new Date();

    @NotNull(message = "tipoMovimiento no debe ser nulo")
    private TipoMovimiento tipoMovimiento;

    @NotNull(message = "valor no debe ser nulo")
    private BigDecimal valor;

    @NotNull(message = "saldoInicial no debe ser nulo")
    private BigDecimal saldoInicial;

    @NotNull(message = "cuentaId no debe ser nulo, un Movmiento pertenece a una Cuenta")
    private Long cuentaId;
}
