package com.fbecvort.springapirest.util;

import com.fbecvort.springapirest.exception.crud.IllegalPaginationArgumentException;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class PaginationUtilsTest {
    private static final int VALID_PAGE = 1;
    private static final int VALID_SIZE = 10;
    private static final String VALID_SORT_BY = "id";
    private static final String VALID_SORT_ORDER = "asc";

    @Test
    public void createPageable() {
        Pageable pageable = PaginationUtils.createPageable(VALID_PAGE, VALID_SIZE, VALID_SORT_BY, VALID_SORT_ORDER);

        assertNotNull(pageable);
        assertEquals(pageable.getPageNumber(), VALID_PAGE);
        assertEquals(pageable.getPageSize(), VALID_SIZE);
        assertEquals(Objects.requireNonNull(pageable.getSort().getOrderFor(VALID_SORT_BY)).getDirection(), Sort.Direction.ASC);
    }

    @Test
    public void createPageable_withInvalidPage() {
        assertThrows(IllegalPaginationArgumentException.class, () ->
                PaginationUtils.createPageable(-1, VALID_SIZE, VALID_SORT_BY, VALID_SORT_ORDER));
    }

    @Test
    public void createPageable_withInvalidSize() {
        assertThrows(IllegalPaginationArgumentException.class, () ->
                PaginationUtils.createPageable(VALID_PAGE, -1, VALID_SORT_BY, VALID_SORT_ORDER));
    }

    @Test
    public void createPageable_withInvalidSortBy() {
        assertThrows(IllegalPaginationArgumentException.class, () ->
                PaginationUtils.createPageable(VALID_PAGE, VALID_SIZE, null, VALID_SORT_ORDER));
    }

    @Test
    public void createPageable_withInvalidSortOrder() {
        assertThrows(IllegalPaginationArgumentException.class, () ->
                PaginationUtils.createPageable(VALID_PAGE, VALID_SIZE, VALID_SORT_BY, null));
    }

    @Test
    public void createPageable_withInvalidSortOrderPattern() {
        assertThrows(IllegalPaginationArgumentException.class, () ->
                PaginationUtils.createPageable(VALID_PAGE, VALID_SIZE, VALID_SORT_BY, "invalid"));
    }
}