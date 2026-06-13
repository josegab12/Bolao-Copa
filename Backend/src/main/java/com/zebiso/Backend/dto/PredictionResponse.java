package com.zebiso.Backend.dto;

import com.zebiso.Backend.model.MatchStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record PredictionResponse(
        UUID id,
        UUID matchId,
        String homeTeam,
        String awayTeam,
        String location,
        LocalDateTime kickoffAt,
        int predictedHomeScore,
        int predictedAwayScore,
        int pointsEarned,
        MatchStatus matchStatus,
        Integer actualHomeScore,
        Integer actualAwayScore
) {
}
