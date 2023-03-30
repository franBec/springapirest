package com.fbecvort.springapirest.dto.retiroanddeposito;

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
public class RetiroAndDepositoResponseDTO {
    String message;
    Long cuentaId;
    Long movimientoId;
}
