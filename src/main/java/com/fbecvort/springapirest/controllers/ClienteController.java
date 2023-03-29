package com.fbecvort.springapirest.controllers;

import com.fbecvort.springapirest.utils.PaginationUtils;
import com.fbecvort.springapirest.dtos.cliente.ClienteRequestDTO;
import com.fbecvort.springapirest.dtos.cliente.ClienteResponseDTO;
import com.fbecvort.springapirest.services.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clientes")
@Validated
public class ClienteController {

    @Autowired
    ClienteService clienteService;

    //[C]RUD -> Create
    @PostMapping
    public ResponseEntity<ClienteResponseDTO> save(@Valid @RequestBody ClienteRequestDTO request) {
        ClienteResponseDTO savedCliente = clienteService.save(request);
        return new ResponseEntity<>(savedCliente, HttpStatus.CREATED);
    }

    //C[R]UD -> READ
    @GetMapping
    public ResponseEntity<Page<ClienteResponseDTO>> findAll(
            @RequestParam(required = false, defaultValue = PaginationUtils.PAGE_DEFAULT_VALUE) int page,
            @RequestParam(required = false, defaultValue = PaginationUtils.SIZE_DEFAULT_VALUE) int size,
            @RequestParam(required = false, defaultValue = PaginationUtils.CLIENTE_SORT_BY_DEFAULT_VALUE) String sortBy,
            @RequestParam(required = false, defaultValue = PaginationUtils.SORT_ORDER_DEFAULT_VALUE) String sortOrder
    ) {
        return ResponseEntity.ok(clienteService.findAll(page, size, sortBy, sortOrder));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> findById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(clienteService.findById(id), HttpStatus.OK);
    }

    //CR[U]D -> Update
    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> update(@PathVariable Long id, @Valid @RequestBody ClienteRequestDTO request) {
        return new ResponseEntity<>(clienteService.update(id, request), HttpStatus.OK);
    }

    //CRU[D] -> Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        clienteService.deleteById(id);
        return ResponseEntity.ok("Cliente con id " + id + " fue eliminado");
    }

}
