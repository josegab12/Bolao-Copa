package com.zebiso.Backend.dto;

import java.time.LocalDateTime;

public record ApiErrorResponse(String message, LocalDateTime timestamp) {

    public static ApiErrorResponse of(String message) {
        return new ApiErrorResponse(message, LocalDateTime.now());
    }
}
