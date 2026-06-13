package com.zebiso.Backend.dto;

import jakarta.validation.constraints.Min;

public record MatchResultRequest(
        @Min(value = 0, message = "Placar nao pode ser negativo")
        int homeScore,
        @Min(value = 0, message = "Placar nao pode ser negativo")
        int awayScore
) {
}
