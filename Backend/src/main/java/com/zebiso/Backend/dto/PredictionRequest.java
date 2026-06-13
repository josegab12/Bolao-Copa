package com.zebiso.Backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record PredictionRequest(
        @NotNull(message = "ID do jogo e obrigatorio")
        UUID matchId,
        @Min(value = 0, message = "Placar nao pode ser negativo")
        int homeScore,
        @Min(value = 0, message = "Placar nao pode ser negativo")
        int awayScore
) {
}
