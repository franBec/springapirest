package com.fbecvort.springapirest.controller;

import com.fbecvort.springapirest.dto.cuenta.CuentaRequestDTO;
import com.fbecvort.springapirest.dto.cuenta.CuentaResponseDTO;
import com.fbecvort.springapirest.dto.retiroanddeposito.RetiroAndDepositoRequestDTO;
import com.fbecvort.springapirest.dto.retiroanddeposito.RetiroAndDepositoResponseDTO;
import com.fbecvort.springapirest.enumeration.TipoMovimiento;
import com.fbecvort.springapirest.service.CuentaService;
import com.fbecvort.springapirest.service.RetiroAndDepositoService;
import com.fbecvort.springapirest.util.PaginationUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cuentas")
public class CuentaController {

    @Autowired
    CuentaService cuentaService;

    @Autowired
    RetiroAndDepositoService retiroAndDepositoService;

    //[C]RUD -> Create
    @PostMapping
    public ResponseEntity<CuentaResponseDTO> save(@Valid @RequestBody CuentaRequestDTO request) {
        return new ResponseEntity<>(cuentaService.save(request), HttpStatus.CREATED);
    }

    //C[R]UD -> READ
    @GetMapping
    public ResponseEntity<Page<CuentaResponseDTO>> findAll(
            @RequestParam(required = false, defaultValue = PaginationUtils.PAGE_DEFAULT_VALUE) int page,
            @RequestParam(required = false, defaultValue = PaginationUtils.SIZE_DEFAULT_VALUE) int size,
            @RequestParam(required = false, defaultValue = PaginationUtils.CUENTA_SORT_BY_DEFAULT_VALUE) String sortBy,
            @RequestParam(required = false, defaultValue = PaginationUtils.SORT_ORDER_DEFAULT_VALUE) String sortOrder
    ) {
        return ResponseEntity.ok(cuentaService.findAll(page, size, sortBy, sortOrder));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CuentaResponseDTO> findById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(cuentaService.findById(id), HttpStatus.OK);
    }

    //CR[U]D -> Update
    @PutMapping("/{id}")
    public ResponseEntity<CuentaResponseDTO> update(@PathVariable Long id, @Valid @RequestBody CuentaRequestDTO request) {
        return new ResponseEntity<>(cuentaService.update(id, request), HttpStatus.OK);
    }

    //CRU[D] -> Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        cuentaService.deleteById(id);
        return ResponseEntity.ok("Cuenta con id " + id + " fue eliminada");
    }

    //Realizar un TipoMovimiento = RETIRO
    @PatchMapping("/{id}/retiro")
    public ResponseEntity<RetiroAndDepositoResponseDTO> realizarRetiro(@PathVariable Long id, @Valid @RequestBody RetiroAndDepositoRequestDTO request) {
        return new ResponseEntity<>(retiroAndDepositoService.realizarMovimiento(id, request.getValor(), TipoMovimiento.RETIRO), HttpStatus.OK);
    }

    //Realizar un TipoMovimiento = DEPOSITO
    @PatchMapping("/{id}/deposito")
    public ResponseEntity<RetiroAndDepositoResponseDTO> realizarDeposito(@PathVariable Long id, @Valid @RequestBody RetiroAndDepositoRequestDTO request) {
        return new ResponseEntity<>(retiroAndDepositoService.realizarMovimiento(id, request.getValor(), TipoMovimiento.DEPOSITO), HttpStatus.OK);
    }
}
