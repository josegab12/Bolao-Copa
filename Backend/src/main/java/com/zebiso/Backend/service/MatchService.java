package com.zebiso.Backend.service;

import com.zebiso.Backend.dto.CreateMatchRequest;
import com.zebiso.Backend.dto.DayMatchesResponse;
import com.zebiso.Backend.dto.MatchResponse;
import com.zebiso.Backend.dto.MatchResultRequest;
import com.zebiso.Backend.exception.ResourceNotFoundException;
import com.zebiso.Backend.model.Match;
import com.zebiso.Backend.model.MatchStatus;
import com.zebiso.Backend.repository.MatchRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class MatchService {

    private final MatchRepository matchRepository;
    private final PredictionService predictionService;

    public MatchService(MatchRepository matchRepository, PredictionService predictionService) {
        this.matchRepository = matchRepository;
        this.predictionService = predictionService;
    }

    public List<MatchResponse> listAll() {
        return matchRepository.findAllByOrderByKickoffAtAsc().stream()
                .map(MatchMapper::toResponse)
                .toList();
    }

    public MatchResponse findById(UUID matchId) {
        return MatchMapper.toResponse(getMatch(matchId));
    }

    public DayMatchesResponse listByDay(LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay();
        List<MatchResponse> matches = matchRepository.findByDay(start, end).stream()
                .map(MatchMapper::toResponse)
                .toList();
        return new DayMatchesResponse(date, matches.size(), matches);
    }

    public List<DayMatchesResponse> listGroupedByDay() {
        Map<LocalDate, List<MatchResponse>> grouped = new LinkedHashMap<>();
        for (Match match : matchRepository.findAllByOrderByKickoffAtAsc()) {
            LocalDate date = match.getKickoffAt().toLocalDate();
            grouped.computeIfAbsent(date, ignored -> new java.util.ArrayList<>())
                    .add(MatchMapper.toResponse(match));
        }

        return grouped.entrySet().stream()
                .map(entry -> new DayMatchesResponse(entry.getKey(), entry.getValue().size(), entry.getValue()))
                .toList();
    }

    @Transactional
    public MatchResponse registerResult(UUID matchId, MatchResultRequest request) {
        Match match = getMatch(matchId);
        match.setHomeScore(request.homeScore());
        match.setAwayScore(request.awayScore());
        match.setStatus(MatchStatus.FINALIZADO);
        matchRepository.save(match);
        predictionService.recalculateForMatch(match);
        predictionService.updateRankingPositions();
        return MatchMapper.toResponse(match);
    }

    @Transactional
    public MatchResponse createMatch(CreateMatchRequest request) {
        String stage = request.stage() != null && !request.stage().isBlank() ? request.stage() : "Eliminatórias";
        Match match = new Match(
                request.homeTeam(),
                request.awayTeam(),
                request.kickoffAt(),
                stage,
                request.groupName(),
                request.location()
        );
        matchRepository.save(match);
        return MatchMapper.toResponse(match);
    }

    Match getMatch(UUID matchId) {
        return matchRepository.findById(matchId)
                .orElseThrow(() -> new ResourceNotFoundException("Jogo nao encontrado"));
    }
}
