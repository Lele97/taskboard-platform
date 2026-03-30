package com.local.taskboard.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)  // nasconde fieldErrors quando null
public record ResponseApiError(
        int status,
        String error,
        String description,
        LocalDateTime timestamp,
        Map<String, String> fieldErrors     // era Map raw → aggiunto <String, String>
) {
    public static ResponseApiError of(int status, String error, String message) {
        return new ResponseApiError(status, error, message, LocalDateTime.now(), null);
    }

    public static ResponseApiError ofValidation(Map<String, String> fieldErrors) {
        return new ResponseApiError(400, "Validation Failed", "Errori sui campi del body",
                LocalDateTime.now(), fieldErrors);
    }
}
