package com.fbecvort.springapirest.exception;

import com.fbecvort.springapirest.exception.bussinessneed.CupoDiarioExcedidoException;
import com.fbecvort.springapirest.exception.crud.EntidadConElementosAsociadosException;
import com.fbecvort.springapirest.exception.crud.NoSuchElementException;
import com.fbecvort.springapirest.exception.bussinessneed.SaldoNoDisponibleException;
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

import java.math.BigDecimal;
import java.util.Arrays;

@Slf4j(topic = "GLOBAL_EXCEPTION_HANDLER")
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Value("${reflectoring.trace:false}")
    private boolean printStackTrace;

    @Value("${springapirest.movimiento.retiro-limite-diario:1000.0}")
    private BigDecimal retiroLimiteDiario;

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
    public ResponseEntity<Object> handleNoSuchElementFoundException(NoSuchElementException ex) {
        log.info("No se pudo encontrar el elemento", ex);

        return buildErrorResponse(
                ex,
                ex.getMessage(),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(EntidadConElementosAsociadosException.class)
    public ResponseEntity<Object> handleEntidadConElementosAsociadosException(EntidadConElementosAsociadosException ex) {
        log.info("No se pudo elminar el elemento", ex);

        return buildErrorResponse(
                ex,
                ex.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(SaldoNoDisponibleException.class)
    public ResponseEntity<Object> handleSaldoNoDisponibleException(SaldoNoDisponibleException ex){
        String logString = "Se intentó debitar " +ex.getMonto().toPlainString() + " de la Cuenta id=" + ex.getId().toString() + " cuyo saldo es de "+ex.getSaldo().toPlainString();
        log.info(logString,ex);

        return buildErrorResponse(
                ex,
                ex.getMessage(),
                HttpStatus.BAD_REQUEST
        );

    }

    @ExceptionHandler(CupoDiarioExcedidoException.class)
    public ResponseEntity<Object> handleCupoDiarioExcedidoException(CupoDiarioExcedidoException ex){
        String logString = "La cuenta id=" + ex.getId().toString()
                + "ha intentado realizar un retiro de " +ex.getMontoARetirar().toPlainString()
                + "lo cual supera el límite diario de retiro de " + retiroLimiteDiario.toPlainString()
                + ". La cantidad que se intentó retirar fue de " +ex.getMontoDiarioAcumulado().toPlainString();

        log.info(logString,ex);

        return buildErrorResponse(
                ex,
                ex.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }

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
