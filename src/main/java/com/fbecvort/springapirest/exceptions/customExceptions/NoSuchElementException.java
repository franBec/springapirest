package com.fbecvort.springapirest.exceptions.customExceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoSuchElementException extends RuntimeException {

    private String resourceName;
    private String fieldName;
    private Object fieldValue;

    public NoSuchElementException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s no encontrado con %s: '%s'", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public NoSuchElementException(){
        super();
    }
}
