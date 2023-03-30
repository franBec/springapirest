package com.fbecvort.springapirest.service;

import com.fbecvort.springapirest.dto.movimiento.MovimientoResponseDTO;
import com.fbecvort.springapirest.dto.movimiento.MovimientoRequestDTO;
import org.springframework.data.domain.Page;

public interface MovimientoService {
    //[C]RUD -> Create
    MovimientoResponseDTO save(MovimientoRequestDTO request);

    //C[R]UD -> READ
    Page<MovimientoResponseDTO> findAll(int page, int size, String sortBy, String sortOrder);
    MovimientoResponseDTO findById(Long id);

    //CR[U]D -> Update
    MovimientoResponseDTO update(Long id, MovimientoRequestDTO request);

    //CRU[D] -> Delete
    void deleteById(Long id);
}
