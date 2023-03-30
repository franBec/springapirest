//https://reflectoring.io/spring-boot-exception-handling/
package com.fbecvort.springapirest.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    //an error code, could be HTTP status code or API custom error code
    private final int status;

    //human-readable message
    private final String message;

    //optional place to print stackTrace when application.properties -> reflectoring.trace = true
    private String stackTrace;

    //handle validation errors
    private List<ValidationError> errors;

    //Definition of a ValidationError
    /*    @Getter
        @Setter
        @RequiredArgsConstructor
        private static class ValidationError {
            private final String field;
            private final String message;
        }*/

    private record ValidationError(String field, String message) {
    }

    public void addValidationError(String field, String message){
        if(Objects.isNull(errors)){
            errors = new ArrayList<>();
        }

        errors.add(new ValidationError(field, message));
    }
}
