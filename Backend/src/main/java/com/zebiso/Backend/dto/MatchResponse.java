package com.zebiso.Backend.dto;

import com.zebiso.Backend.model.MatchStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record MatchResponse(
        UUID id,
        String homeTeam,
        String awayTeam,
        LocalDateTime kickoffAt,
        LocalDate date,
        String stage,
        String groupName,
        String location,
        MatchStatus status,
        Integer homeScore,
        Integer awayScore
) {
}
