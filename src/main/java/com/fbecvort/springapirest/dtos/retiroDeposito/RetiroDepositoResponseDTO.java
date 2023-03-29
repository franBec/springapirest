package com.fbecvort.springapirest.dtos.retiroDeposito;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class RetiroDepositoResponseDTO {
    String message;
    Long cuentaId;
    Long movimientoId;
}
