package com.fbecvort.springapirest.util;

import com.fbecvort.springapirest.exception.crud.IllegalPaginationArgumentException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;

@Validated
public class PaginationUtils {
    private PaginationUtils() {}

    // Instance of Pageable used by all the findAll() methods
    public static Pageable createPageable(int page, int size, String sortBy, String sortOrder) {
        if(page < PAGE_MIN_VALUE)
            throw new IllegalPaginationArgumentException(page, PAGE_MIN_MESSAGE);

        if(size < SIZE_MIN_VALUE)
            throw new IllegalPaginationArgumentException(size, SIZE_MIN_MESSAGE);

        if(size > SIZE_MAX_VALUE)
            throw new IllegalPaginationArgumentException(size, SIZE_MAX_MESSAGE);

        if(Objects.isNull(sortBy))
            throw new IllegalPaginationArgumentException(null, SORT_BY_NOT_NULL_MESSAGE);

        if(Objects.isNull(sortOrder))
            throw new IllegalPaginationArgumentException(null, SORT_ORDER_NOT_NULL_MESSAGE);

        if(!sortOrder.matches(SORT_ORDER_PATTERN_REGEXP_VALUE))
            throw new IllegalPaginationArgumentException(sortOrder, SORT_ORDER_PATTERN_REGEXP_MESSAGE);

        return PageRequest.of(
                page,
                size,
                sortOrder.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC,
                sortBy
        );
    }

    // COMMON CONSTANTS ACROSS ALL PAGINATION METHODS

    public static final int PAGE_MIN_VALUE = 0;
    public static final String PAGE_MIN_MESSAGE = "El número de página debe ser mayor o igual a cero";

    public static final String PAGE_DEFAULT_VALUE = "0";

    public static final int SIZE_MIN_VALUE = 1;
    public static final String SIZE_MIN_MESSAGE = "El tamaño de página debe ser mayor o igual a uno";

    public static final int SIZE_MAX_VALUE = 100;
    public static final String SIZE_MAX_MESSAGE = "El tamaño de página debe ser menor o igual a 100";

    public static final String SIZE_DEFAULT_VALUE = "10";

    public static final String SORT_BY_NOT_NULL_MESSAGE = "El parámetro sortBy no puede ser nulo";

    public static final String SORT_ORDER_NOT_NULL_MESSAGE = "El parámetro sortOrder no puede ser nulo";

    public static final String SORT_ORDER_PATTERN_REGEXP_VALUE = "asc|desc";
    public static final String SORT_ORDER_PATTERN_REGEXP_MESSAGE = "El parámetro sortOrder debe ser 'asc' o 'desc'";

    public static final String SORT_ORDER_DEFAULT_VALUE = "asc";

    // SPECIFIC SORT_BY OF EACH ENTITY

    public static final String CLIENTE_SORT_BY_DEFAULT_VALUE = "personaId";
    public static final String CUENTA_SORT_BY_DEFAULT_VALUE = "cuentaId";
    public static final String MOVIMIENTO_SORT_BY_ORDER_DEFAULT_VALUE = "movimientoId";


}
