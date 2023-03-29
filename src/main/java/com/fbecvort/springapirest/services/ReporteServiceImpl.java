package com.fbecvort.springapirest.services;

import com.fbecvort.springapirest.dtos.reporte.ReporteCuentaResponseDTO;
import com.fbecvort.springapirest.entities.Cuenta;
import com.fbecvort.springapirest.entities.Movimiento;
import com.fbecvort.springapirest.enums.TipoMovimiento;
import com.fbecvort.springapirest.exceptions.customExceptions.NoSuchElementException;
import com.fbecvort.springapirest.repositories.ClienteRepository;
import com.fbecvort.springapirest.repositories.CuentaRepository;
import com.fbecvort.springapirest.utils.DateUtils;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ReporteServiceImpl implements ReporteService {

    @Autowired
    ClienteRepository clienteRepository;

    @Autowired
    CuentaRepository cuentaRepository;

    @Override
    public List<ReporteCuentaResponseDTO> makeReporte(Long clienteId, Date start, Date end) {

        List<Cuenta> cuentas = cuentaRepository
                .findAllByCliente(
                        clienteRepository
                                .findById(clienteId)
                                .orElseThrow(()-> new NoSuchElementException("Cliente", "id", clienteId))
                );


        return cuentas
                .stream()
                .map(cuenta -> cuentaToReporteCuentaResponseDto(cuenta, start, end))
                .collect(Collectors.toList());
    }

    private ReporteCuentaResponseDTO cuentaToReporteCuentaResponseDto(Cuenta cuenta, Date start, Date end) {
        ReporteCuentaResponseDTO response = new DozerBeanMapper().map(cuenta, ReporteCuentaResponseDTO.class);

        response.setPeriodoStart(start);
        response.setPeriodoEnd(end);

        response.setTotalDepositosEnPeriodo(howMuchMovimientoValorBetweenDates(cuenta, TipoMovimiento.DEPOSITO, start, end));
        response.setTotalRetirosEnPeriodo(howMuchMovimientoValorBetweenDates(cuenta, TipoMovimiento.RETIRO, start, end));

        return response;
    }

    private BigDecimal howMuchMovimientoValorBetweenDates(Cuenta cuenta, TipoMovimiento tipoMovimiento, Date start, Date end) {
        return cuenta
                .getMovimientos()
                .stream()
                .filter(
                        movimiento -> Objects.equals(movimiento.getTipoMovimiento(), tipoMovimiento) && DateUtils.checkIfDateHappenedBetweenTwoDates(movimiento.getFecha(), start, end)
                )
                .map(Movimiento::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
