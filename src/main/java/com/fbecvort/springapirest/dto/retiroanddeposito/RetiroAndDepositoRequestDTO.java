package com.fbecvort.springapirest.dto.retiroanddeposito;

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
public class RetiroAndDepositoRequestDTO {
    private BigDecimal valor;
}
