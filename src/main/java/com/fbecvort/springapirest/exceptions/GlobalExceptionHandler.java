package com.fbecvort.springapirest.exceptions;

import com.fbecvort.springapirest.exceptions.customExceptions.NoSuchElementException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;

@Slf4j(topic = "GLOBAL_EXCEPTION_HANDLER")
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Value("${reflectoring.trace:false}")
    private boolean printStackTrace;

    // ---- Error response builder ----
    private ResponseEntity<Object> buildErrorResponse(Exception exception, String message, HttpStatusCode httpStatus) {
        ErrorResponse errorResponse = new ErrorResponse(httpStatus.value(), message);

        if (printStackTrace) {
            errorResponse.setStackTrace(Arrays.toString(exception.getStackTrace()));
        }

        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    // ---- Overriden handlers ----

    @Override
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Validation error. Check 'errors' field for details."
        );

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errorResponse.addValidationError(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return ResponseEntity
                .unprocessableEntity()
                .body(errorResponse);
    }

    @Override
    public ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        return buildErrorResponse(ex, ex.getMessage(), statusCode);
    }

    // ---- NOT overriden handlers ----

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Object> handleNoSuchElementFoundException(NoSuchElementException itemNotFoundException) {
        log.error("Failed to find the requested element", itemNotFoundException);

        return buildErrorResponse(
                itemNotFoundException,
                itemNotFoundException.getMessage(),
                HttpStatus.NOT_FOUND
        );
    }
/*

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException illegalArgumentException) {

        return buildErrorResponse(
                illegalArgumentException,
                illegalArgumentException.getMessage(),
                HttpStatus.BAD_REQUEST
        );

    }
*/

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleAllUncaughtException(Exception exception) {
        log.error("Unknown error occurred", exception);

        return buildErrorResponse(
                exception,
                "Unknown error occurred",
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
