package com.zebiso.Backend.dto;

import java.util.UUID;

public record MatchPredictionResponse(
        UUID id,
        UUID userId,
        String userName,
        String userAvatar,
        int predictedHomeScore,
        int predictedAwayScore,
        int pointsEarned
) {
}
