package com.local.taskboard.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@Component
@Order(-1)
public class GatewayErrorWebExceptionHandler implements WebExceptionHandler {

    private final ObjectMapper objectMapper;

    public GatewayErrorWebExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        HttpStatus status;
        ResponseApiError apiError;

        if (ex instanceof WebExchangeBindException bindEx) {
            status = HttpStatus.BAD_REQUEST;
            var fieldErrors = bindEx.getBindingResult().getFieldErrors()
                    .stream()
                    .collect(Collectors.toMap(
                            FieldError::getField,
                            fe -> fe.getDefaultMessage() != null ? fe.getDefaultMessage() : "Valore non valido",
                            (m1, m2) -> m1
                    ));
            apiError = ResponseApiError.ofValidation(fieldErrors);

        } else if (ex instanceof UsernameNotFoundException || ex instanceof BadCredentialsException) {
            status = HttpStatus.UNAUTHORIZED;
            apiError = ResponseApiError.of(401, "Unauthorized", ex.getMessage());

        } else if (ex instanceof RuntimeException) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            apiError = ResponseApiError.of(500, "Internal Server Error", ex.getMessage());

        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            apiError = ResponseApiError.of(500, "Internal Server Error", "Errore generico");
        }

        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        byte[] bytes;
        try {
            bytes = objectMapper.writeValueAsBytes(apiError);
        } catch (JsonProcessingException e) {
            bytes = "{\"error\":\"Serialization error\"}".getBytes();
        }

        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
        return exchange.getResponse().writeWith(Mono.just(buffer));
    }
}
