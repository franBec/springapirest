package com.fbecvort.springapirest.services;

import com.fbecvort.springapirest.dtos.cuenta.CuentaRequestDTO;
import com.fbecvort.springapirest.dtos.cuenta.CuentaResponseDTO;
import com.fbecvort.springapirest.entities.Cuenta;
import com.fbecvort.springapirest.entities.Movimiento;
import com.fbecvort.springapirest.enums.TipoMovimiento;
import com.fbecvort.springapirest.exceptions.customExceptions.CupoDiarioExcedidoException;
import com.fbecvort.springapirest.exceptions.customExceptions.NoSuchElementException;
import com.fbecvort.springapirest.exceptions.customExceptions.SaldoNoDisponibleException;
import com.fbecvort.springapirest.repositories.ClienteRepository;
import com.fbecvort.springapirest.repositories.CuentaRepository;
import com.fbecvort.springapirest.repositories.MovimientoRepository;
import com.fbecvort.springapirest.utils.DateUtils;
import com.fbecvort.springapirest.utils.PaginationUtils;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CuentaServiceImpl implements CuentaService{

    @Autowired
    CuentaRepository cuentaRepository;

    @Autowired
    ClienteRepository clienteRepository;

    @Autowired
    MovimientoRepository movimientoRepository;

    @Value("${springapirest.movimiento.retiro-limite-diario:1000.0}")
    private BigDecimal retiroLimiteDiario;

    @Override
    @Transactional
    public CuentaResponseDTO save(CuentaRequestDTO request) {
        Cuenta newCuenta = new DozerBeanMapper().map(request, Cuenta.class);
        newCuenta.setCliente(
                clienteRepository
                        .findById(request.getClienteId())
                        .orElseThrow(()-> new NoSuchElementException("Cliente", "id", request.getClienteId()))
        );

        return cuentaToResponseDTO(cuentaRepository.save(newCuenta));
    }

    @Override
    public Page<CuentaResponseDTO> findAll(int page, int size, String sortBy, String sortOrder) {
        Pageable pageable = PaginationUtils.createPageable(page, size, sortBy, sortOrder);

        Page<Cuenta> cuentas = cuentaRepository.findAll(pageable);

        return new PageImpl<>(
                cuentas.getContent().stream().map(this::cuentaToResponseDTO).collect(Collectors.toList()),
                pageable,
                cuentas.getTotalElements()
        );
    }

    @Override
    public CuentaResponseDTO findById(Long id) {
        return cuentaToResponseDTO(
                cuentaRepository
                        .findById(id)
                        .orElseThrow(()->new NoSuchElementException("Cuenta","id", id))
        );
    }

    @Override
    @Transactional
    public CuentaResponseDTO update(Long id, CuentaRequestDTO request) {
        // recupero Cuenta a actualizar
        Cuenta currentCuenta = cuentaRepository
                .findById(id)
                .orElseThrow(()->new NoSuchElementException("Cliente","id", id));

        // creo una nueva instancia de Cuenta a partir del request
        Cuenta updatedCuenta = new DozerBeanMapper().map(request, Cuenta.class);

        // a esta nueva instancia le tengo que setear cuentaId
        updatedCuenta.setCuentaId(currentCuenta.getCuentaId());

        /*
            A esta nueva instancia de Cuenta le tengo que setear Cliente, aca hay dos posibilidades

            escenario 1: la Cuenta le pertenece a un nuevo Cliente
            escenario 2: la Cuenta sigue con el mismo Cliente
        */

        if(!Objects.equals(currentCuenta.getCliente().getPersonaId(), request.getClienteId())){

            // escenario 1: la Cuenta le pertenece a un nuevo Cliente
            updatedCuenta.setCliente(
                    clienteRepository
                            .findById(request.getClienteId())
                            .orElseThrow(()-> new NoSuchElementException("Cuenta", "id", request.getClienteId()))
            );
        }
        else{

            // escenario 2: la Cuenta sigue con el mismo Cliente
            updatedCuenta.setCliente(currentCuenta.getCliente());
        }

        // update and return
        return cuentaToResponseDTO(cuentaRepository.save(updatedCuenta));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if(!cuentaRepository.existsById(id)){
            throw new NoSuchElementException("Cuenta", "id", id);
        }

        cuentaRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void realizarMovimiento(Long id, BigDecimal valor, TipoMovimiento tipoMovimiento) {
        Cuenta cuenta = cuentaRepository
                .findById(id)
                .orElseThrow(()-> new NoSuchElementException("Cuenta", "id", id));

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

        movimientoRepository.save(movimiento);
        cuentaRepository.save(cuenta);

    }

    private CuentaResponseDTO cuentaToResponseDTO(Cuenta cuenta) {
        CuentaResponseDTO response = new DozerBeanMapper().map(cuenta, CuentaResponseDTO.class);

        response.setClienteId(cuenta.getCliente().getPersonaId());
        response.setMovimientosId(
                cuenta
                        .getMovimientos()
                        .stream()
                        .map(Movimiento::getMovimientoId)
                        .collect(Collectors.toSet())
        );

        return response;
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
                        movimiento -> movimiento.getTipoMovimiento() == TipoMovimiento.RETIRO && DateUtils.checkIfDateHappenedToday(movimiento.getFecha())
                )
                .map(Movimiento::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
