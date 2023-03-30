package com.fbecvort.springapirest.service;

import com.fbecvort.springapirest.dto.cuenta.CuentaRequestDTO;
import com.fbecvort.springapirest.dto.cuenta.CuentaResponseDTO;
import com.fbecvort.springapirest.entity.Cuenta;
import com.fbecvort.springapirest.entity.Movimiento;
import com.fbecvort.springapirest.exception.crud.EntityWithAssociatedElementsException;
import com.fbecvort.springapirest.exception.crud.NoSuchElementException;
import com.fbecvort.springapirest.repository.ClienteRepository;
import com.fbecvort.springapirest.repository.CuentaRepository;
import com.fbecvort.springapirest.repository.MovimientoRepository;
import com.fbecvort.springapirest.util.PaginationUtils;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        Cuenta cuenta = cuentaRepository
                .findById(id)
                .orElseThrow(()-> new NoSuchElementException("Cuenta", "id", id));

        if(!cuenta.getMovimientos().isEmpty()) {
            throw new EntityWithAssociatedElementsException("Cuenta", id, "Movimiento");
        }

        cuentaRepository.deleteById(id);
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
}
