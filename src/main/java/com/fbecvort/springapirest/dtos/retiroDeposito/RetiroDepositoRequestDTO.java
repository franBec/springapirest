package com.fbecvort.springapirest.dtos.retiroDeposito;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RetiroDepositoRequestDTO {
    private BigDecimal valor;
}
