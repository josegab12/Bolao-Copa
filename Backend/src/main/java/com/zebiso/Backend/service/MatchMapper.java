package com.zebiso.Backend.service;

import com.zebiso.Backend.dto.MatchResponse;
import com.zebiso.Backend.model.Match;

import java.time.LocalDate;

final class MatchMapper {

    private MatchMapper() {
    }

    static MatchResponse toResponse(Match match) {
        return new MatchResponse(
                match.getId(),
                match.getHomeTeam(),
                match.getAwayTeam(),
                match.getKickoffAt(),
                match.getKickoffAt().toLocalDate(),
                match.getStage(),
                match.getGroupName(),
                match.getLocation(),
                match.getStatus(),
                match.getHomeScore(),
                match.getAwayScore()
        );
    }
}
