package com.fbecvort.springapirest.controllers;

import com.fbecvort.springapirest.dtos.movimiento.MovimientoRequestDTO;
import com.fbecvort.springapirest.dtos.movimiento.MovimientoResponseDTO;
import com.fbecvort.springapirest.services.MovimientoService;
import com.fbecvort.springapirest.utils.PaginationUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/api/movimientos")
public class MovimientoController {

    @Autowired
    MovimientoService movimientoService;

    //[C]RUD -> Create
    @PostMapping
    public ResponseEntity<MovimientoResponseDTO> save(@Valid @RequestBody MovimientoRequestDTO request) {
        return new ResponseEntity<>(movimientoService.save(request), HttpStatus.CREATED);
    }

    //C[R]UD -> READ
    @GetMapping
    public ResponseEntity<Page<MovimientoResponseDTO>> findAll(
            @RequestParam(required = false, defaultValue = PaginationUtils.PAGE_DEFAULT_VALUE) int page,
            @RequestParam(required = false, defaultValue = PaginationUtils.SIZE_DEFAULT_VALUE) int size,
            @RequestParam(required = false, defaultValue = PaginationUtils.MOVIMIENTO_SORT_BY_ORDER_DEFAULT_VALUE) String sortBy,
            @RequestParam(required = false, defaultValue = PaginationUtils.SORT_ORDER_DEFAULT_VALUE) String sortOrder
    ) {
        return ResponseEntity.ok(movimientoService.findAll(page, size, sortBy, sortOrder));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovimientoResponseDTO> findById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(movimientoService.findById(id), HttpStatus.OK);
    }

    //CR[U]D -> Update
    @PutMapping("/{id}")
    public ResponseEntity<MovimientoResponseDTO> update(@PathVariable Long id, @Valid @RequestBody MovimientoRequestDTO request) {
        return new ResponseEntity<>(movimientoService.update(id, request), HttpStatus.OK);
    }

    //CRU[D] -> Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        movimientoService.deleteById(id);
        return ResponseEntity.ok("Movimiento con id " + id + " fue eliminado");
    }
}
