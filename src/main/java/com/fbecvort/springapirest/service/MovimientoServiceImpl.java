package com.fbecvort.springapirest.service;

import com.fbecvort.springapirest.dto.movimiento.MovimientoRequestDTO;
import com.fbecvort.springapirest.dto.movimiento.MovimientoResponseDTO;
import com.fbecvort.springapirest.entity.Movimiento;
import com.fbecvort.springapirest.exception.crud.NoSuchElementException;
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
public class MovimientoServiceImpl implements MovimientoService{

    @Autowired
    MovimientoRepository movimientoRepository;

    @Autowired
    CuentaRepository cuentaRepository;

    @Override
    @Transactional
    public MovimientoResponseDTO save(MovimientoRequestDTO request) {
        Movimiento newMovimiento = new DozerBeanMapper().map(request, Movimiento.class);
        newMovimiento.setCuenta(
                cuentaRepository
                        .findById(request.getCuentaId())
                        .orElseThrow(()-> new NoSuchElementException("Cuenta", "id", request.getCuentaId()))
        );

        return movimientoToResponseDTO(movimientoRepository.save(newMovimiento));
    }

    @Override
    public Page<MovimientoResponseDTO> findAll(int page, int size, String sortBy, String sortOrder) {
        Pageable pageable = PaginationUtils.createPageable(page, size, sortBy, sortOrder);

        Page<Movimiento> movimientos = movimientoRepository.findAll(pageable);

        return new PageImpl<>(
                movimientos.getContent().stream().map(this::movimientoToResponseDTO).collect(Collectors.toList()),
                pageable,
                movimientos.getTotalElements()
        );
    }

    @Override
    public MovimientoResponseDTO findById(Long id) {
        return movimientoToResponseDTO(
                movimientoRepository
                        .findById(id)
                        .orElseThrow(()->new NoSuchElementException("Movimiento", "id", id))
        );
    }

    @Override
    @Transactional
    public MovimientoResponseDTO update(Long id, MovimientoRequestDTO request) {
        // recupero Movimiento a actualizar
        Movimiento currentMovimiento = movimientoRepository
                .findById(id)
                .orElseThrow(()->new NoSuchElementException("Movimiento", "id", id));

        // creo una nueva instancia de Movimiento a partir del request
        Movimiento updatedMovimiento = new DozerBeanMapper().map(request, Movimiento.class);

        // a esta nueva instancia le tengo que setear movimientoId
        updatedMovimiento.setMovimientoId(currentMovimiento.getMovimientoId());

        /*
            A esta nueva instancia de Movimiento le tengo que setear Cuenta, aca hay dos posibilidades

            escenario 1: el Movimiento le pertenece a una nueva Cuenta
            escenario 2: el Movimiento sigue con la misma Cuenta
        */

        if(!Objects.equals(currentMovimiento.getCuenta().getCuentaId(), request.getCuentaId())){

            // escenario 1: el Movimiento le pertenece a una nueva Cuenta
            updatedMovimiento.setCuenta(
                    cuentaRepository
                            .findById(request.getCuentaId())
                            .orElseThrow(()->new NoSuchElementException("Cuenta", "id", id))
            );
        }
        else{

            // escenario 2: el Movimiento sigue con la misma Cuenta
            updatedMovimiento.setCuenta(currentMovimiento.getCuenta());
        }

        // update and return
        return movimientoToResponseDTO(movimientoRepository.save(updatedMovimiento));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if(!movimientoRepository.existsById(id)){
            throw new NoSuchElementException("Movimiento", "id", id);
        }

        movimientoRepository.deleteById(id);
    }

    private MovimientoResponseDTO movimientoToResponseDTO(Movimiento movimiento){
        MovimientoResponseDTO response = new DozerBeanMapper().map(movimiento, MovimientoResponseDTO.class);

        response.setCuentaId(movimiento.getCuenta().getCuentaId());

        return response;
    }

}
