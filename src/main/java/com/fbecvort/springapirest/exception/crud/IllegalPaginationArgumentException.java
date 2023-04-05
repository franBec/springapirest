package com.fbecvort.springapirest.exception.crud;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class IllegalPaginationArgumentException extends RuntimeException {

    public IllegalPaginationArgumentException(){
        super();
    }

    public IllegalPaginationArgumentException(Object value, String message){
        super(String.format("Valor inválido %s. %s", value, message));
    }
}
