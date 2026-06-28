package com.zebiso.Backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record CreateMatchRequest(
        @NotBlank(message = "Time mandante é obrigatório")
        String homeTeam,
        
        @NotBlank(message = "Time visitante é obrigatório")
        String awayTeam,
        
        @NotNull(message = "Horário de início é obrigatório")
        LocalDateTime kickoffAt,
        
        String stage,
        String groupName,
        String location
) {
}
