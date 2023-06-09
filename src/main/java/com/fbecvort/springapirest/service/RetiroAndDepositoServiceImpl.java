package com.fbecvort.springapirest.service;

import com.fbecvort.springapirest.dto.retiroanddeposito.RetiroAndDepositoResponseDTO;
import com.fbecvort.springapirest.entity.Cuenta;
import com.fbecvort.springapirest.entity.Movimiento;
import com.fbecvort.springapirest.enumeration.TipoMovimiento;
import com.fbecvort.springapirest.exception.bussinessneed.CupoDiarioExcedidoException;
import com.fbecvort.springapirest.exception.crud.NoSuchElementException;
import com.fbecvort.springapirest.exception.bussinessneed.SaldoNoDisponibleException;
import com.fbecvort.springapirest.repository.CuentaRepository;
import com.fbecvort.springapirest.repository.MovimientoRepository;
import com.fbecvort.springapirest.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Objects;

@Service
public class RetiroAndDepositoServiceImpl implements RetiroAndDepositoService{

    @Value("${springapirest.movimiento.retiro-limite-diario:1000.0}")
    private BigDecimal retiroLimiteDiario;

    @Autowired
    CuentaRepository cuentaRepository;

    @Autowired
    MovimientoRepository movimientoRepository;

    @Override
    @Transactional
    public RetiroAndDepositoResponseDTO realizarMovimiento(Long cuentaId, BigDecimal valor, TipoMovimiento tipoMovimiento) {
        Cuenta cuenta = cuentaRepository
                .findById(cuentaId)
                .orElseThrow(()-> new NoSuchElementException("Cuenta", "id", cuentaId));

        Movimiento movimiento = Movimiento
                .builder()
                .tipoMovimiento(tipoMovimiento)
                .valor(valor)
                .saldoInicial(cuenta.getSaldo())
                .cuenta(cuenta)
                .build();

        if(tipoMovimiento.equals(TipoMovimiento.DEPOSITO)){
            cuenta.setSaldo(cuenta.getSaldo().add(valor));
        }
        else{
            checkIfRetiroIsPossible(cuenta, valor);
            cuenta.setSaldo(cuenta.getSaldo().subtract(valor));
        }

        Long movimientoId = movimientoRepository.save(movimiento).getMovimientoId();
        cuentaRepository.save(cuenta);

        return RetiroAndDepositoResponseDTO
                .builder()
                .message(createMessageForRetiroDepositoResponse(cuentaId, movimientoId, tipoMovimiento))
                .movimientoId(movimientoId)
                .cuentaId(cuentaId)
                .build();
    }

    private void checkIfRetiroIsPossible(Cuenta cuenta, BigDecimal valor) {
        /*
            "Si el saldo es cero, y va a realizar un debito, debe desplegar mensaje "Saldo no disponible"
            Procedo a interpretar esto como que cuenta.saldo no puede ser negativo
         */
        if(cuenta.getSaldo().subtract(valor).compareTo(BigDecimal.ZERO) < 0) {
            throw new SaldoNoDisponibleException(cuenta.getCuentaId(), cuenta.getSaldo(), valor);
        }

        /*
            "Se debe tener un parámetro de límite diario de retiro. Si el cupo disponible ya se cumplió
            no se debe permitir realizar un debito y debe desplegar el mensaje "Cupo diario Excedido" "
         */
        BigDecimal montoDiarioAcumulado = howMuchHasBeenRetiradoToday(cuenta);
        if(montoDiarioAcumulado.add(valor).compareTo(retiroLimiteDiario) > 0){
            throw new CupoDiarioExcedidoException(cuenta.getCuentaId(), montoDiarioAcumulado, valor);
        }
    }

    private BigDecimal howMuchHasBeenRetiradoToday(Cuenta cuenta){

        return cuenta
                .getMovimientos()
                .stream()
                .filter(
                        movimiento -> Objects.equals(movimiento.getTipoMovimiento(), TipoMovimiento.RETIRO) && DateUtils.checkIfDateHappenedToday(movimiento.getFecha())
                )
                .map(Movimiento::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private String createMessageForRetiroDepositoResponse(Long cuentaId, Long movimientoId, TipoMovimiento tipoMovimiento){
        return "La cuenta id="+cuentaId.toString()+" ha realizado un "+tipoMovimiento.toString()+" exitosamente. Id del movimiento="+movimientoId.toString();
    }
}
