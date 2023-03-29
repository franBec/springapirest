package com.fbecvort.springapirest.services;

import com.fbecvort.springapirest.dtos.cuenta.CuentaRequestDTO;
import com.fbecvort.springapirest.dtos.cuenta.CuentaResponseDTO;
import org.springframework.data.domain.Page;

public interface CuentaService {

    //[C]RUD -> Create
    CuentaResponseDTO save(CuentaRequestDTO request);

    //C[R]UD -> READ
    Page<CuentaResponseDTO> findAll(int page, int size, String sortBy, String sortOrder);
    CuentaResponseDTO findById(Long id);

    //CR[U]D -> Update
    CuentaResponseDTO update(Long id, CuentaRequestDTO request);

    //CRU[D] -> Delete
    void deleteById(Long id);
}
