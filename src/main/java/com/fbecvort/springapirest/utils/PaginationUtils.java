package com.fbecvort.springapirest.utils;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;

@Validated
public class PaginationUtils {
    private PaginationUtils() {}

    // Instance of Pageable used by all the findAll() methods
    public static Pageable createPageable(
            @Min(value = 0, message = PAGE_MIN_VALUE_MESSAGE)
            @Valid int page,

            @Min(value = SIZE_MIN_VALUE, message = SIZE_MIN_MESSAGE)
            @Max(value = SIZE_MAX_VALUE, message = SIZE_MAX_MESSAGE)
            @Valid int size,

            @NotNull(message = SORT_BY_NOT_NULL_MESSAGE)
            @Valid String sortBy,

            @NotNull(message = SORT_ORDER_NOT_NULL_MESSAGE)
            @Pattern(regexp = SORT_ORDER_PATTERN_REGEXP_VALUE, message = SORT_ORDER_PATTERN_REGEXP_MESSAGE)
            @Valid String sortOrder
    ) {
        return PageRequest.of(
                page,
                size,
                sortOrder.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC,
                sortBy
        );
    }


    // COMMON CONSTANTS ACROSS ALL PAGINATION METHODS

    public static final String PAGE_MIN_VALUE_MESSAGE = "El número de página debe ser mayor o igual a cero";
    public static final String PAGE_DEFAULT_VALUE = "0";

    public static final int SIZE_MIN_VALUE = 1;
    public static final String SIZE_MIN_MESSAGE = "El tamaño de página debe ser mayor o igual a uno";

    public static final int SIZE_MAX_VALUE = 100;
    public static final String SIZE_MAX_MESSAGE = "El tamaño de página debe ser menor o igual a 100";

    public static final String SIZE_DEFAULT_VALUE = "10";

    public static final String SORT_BY_NOT_NULL_MESSAGE = "El parámetro sortBy no puede ser nulo";

    public static final String SORT_ORDER_NOT_NULL_MESSAGE = "El parámetro sortOrder no puede ser nulo";

    public static final String SORT_ORDER_PATTERN_REGEXP_MESSAGE = "El parámetro sortOrder debe ser 'asc' o 'desc'";
    public static final String SORT_ORDER_PATTERN_REGEXP_VALUE = "asc|desc";

    public static final String SORT_ORDER_DEFAULT_VALUE = "asc";

    // SPECIFIC SORT_BY OF EACH ENTITY

    public static final String CLIENTE_SORT_BY_DEFAULT_VALUE = "personaId";
    public static final String CUENTA_SORT_BY_DEFAULT_VALUE = "cuentaId";
    public static final String MOVIMIENTO_SORT_BY_ORDER_DEFAULT_VALUE = "movimientoId";


}
