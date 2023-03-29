package com.fbecvort.springapirest.services;

import com.fbecvort.springapirest.dtos.cliente.ClienteResponseDTO;
import com.fbecvort.springapirest.dtos.cliente.ClienteRequestDTO;
import org.springframework.data.domain.Page;

public interface ClienteService {

    //[C]RUD -> Create
    ClienteResponseDTO save(ClienteRequestDTO request);

    //C[R]UD -> READ
    Page<ClienteResponseDTO> findAll(int page, int size, String sortBy, String sortOrder);
    ClienteResponseDTO findById(Long id);

    //CR[U]D -> Update
    ClienteResponseDTO update(Long id, ClienteRequestDTO request);

    //CRU[D] -> Delete
    void deleteById(Long id);
}
