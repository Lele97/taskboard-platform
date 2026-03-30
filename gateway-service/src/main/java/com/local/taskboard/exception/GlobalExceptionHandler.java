package com.local.taskboard.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<ResponseApiError>> handleValidation(WebExchangeBindException ex) {
        Map<String, String> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        fe -> fe.getDefaultMessage() != null ? fe.getDefaultMessage() : "Valore non valido",
                        (msg1, msg2) -> msg1
                ));
        return Mono.just(ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ResponseApiError.ofValidation(fieldErrors)));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public Mono<ResponseEntity<ResponseApiError>> handleBadCredentials(BadCredentialsException ex) {
        return Mono.just(ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ResponseApiError.of(401, "Bad Credentials", ex.getMessage())));
    }

    @ExceptionHandler(WebClientResponseException.class)
    public Mono<ResponseEntity<ResponseApiError>> handleWebClientResponse(WebClientResponseException ex) {
        HttpStatus status = HttpStatus.resolve(ex.getStatusCode().value());
        return Mono.just(ResponseEntity
                .status(ex.getStatusCode())
                .contentType(MediaType.APPLICATION_JSON)
                .body(ResponseApiError.of(
                        ex.getStatusCode().value(),
                        status != null ? status.getReasonPhrase() : "Error",
                        ex.getResponseBodyAsString()
                )));
    }

    @ExceptionHandler(RuntimeException.class)
    public Mono<ResponseEntity<ResponseApiError>> handleRuntime(RuntimeException ex) {
        return Mono.just(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ResponseApiError.of(500, "Internal Server Error", ex.getMessage())));
    }
}
