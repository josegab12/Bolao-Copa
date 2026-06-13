package com.zebiso.Backend.service;

import com.zebiso.Backend.dto.PredictionRequest;
import com.zebiso.Backend.dto.PredictionResponse;
import com.zebiso.Backend.dto.RankingEntryResponse;
import com.zebiso.Backend.exception.BusinessException;
import com.zebiso.Backend.model.Match;
import com.zebiso.Backend.model.Prediction;
import com.zebiso.Backend.model.User;
import com.zebiso.Backend.repository.PredictionRepository;
import com.zebiso.Backend.repository.RankingProjection;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PredictionService {

    private final PredictionRepository predictionRepository;
    private final MatchService matchService;
    private final UserService userService;
    private final ScoringService scoringService;

    public PredictionService(
            PredictionRepository predictionRepository,
            @Lazy MatchService matchService,
            UserService userService,
            ScoringService scoringService) {
        this.predictionRepository = predictionRepository;
        this.matchService = matchService;
        this.userService = userService;
        this.scoringService = scoringService;
    }

    @Transactional
    public PredictionResponse save(UUID userId, PredictionRequest request) {
        User user = userService.findById(userId);
        Match match = matchService.getMatch(request.matchId());

        if (match.getStatus() == com.zebiso.Backend.model.MatchStatus.FINALIZADO) {
            throw new BusinessException("Nao e possivel palpitar em jogo ja finalizado");
        }
        if (match.getKickoffAt().isBefore(LocalDateTime.now())) {
            throw new BusinessException("Nao e possivel palpitar apos o inicio do jogo");
        }

        Prediction prediction = predictionRepository.findByUserIdAndMatchId(userId, request.matchId())
                .orElseGet(() -> new Prediction(user, match, request.homeScore(), request.awayScore()));

        prediction.setPredictedHomeScore(request.homeScore());
        prediction.setPredictedAwayScore(request.awayScore());

        if (match.isFinished() && match.getHomeScore() != null && match.getAwayScore() != null) {
            prediction.setPointsEarned(scoringService.calculatePoints(
                    prediction.getPredictedHomeScore(),
                    prediction.getPredictedAwayScore(),
                    match.getHomeScore(),
                    match.getAwayScore()));
        }

        return toResponse(predictionRepository.save(prediction));
    }

    public List<PredictionResponse> listByUser(UUID userId) {
        userService.findById(userId);
        return predictionRepository.findByUserIdOrderByMatch_KickoffAtAsc(userId).stream()
                .map(this::toResponse)
                .toList();
    }

    public List<RankingEntryResponse> ranking() {
        List<RankingProjection> rows = predictionRepository.findRanking();
        List<RankingEntryResponse> ranking = new ArrayList<>();
        int position = 1;
        for (RankingProjection row : rows) {
            ranking.add(new RankingEntryResponse(
                    position++,
                    row.getUserId(),
                    row.getName(),
                    row.getAvatar(),
                    row.getTotalPoints()));
        }
        return ranking;
    }

    @Transactional
    void recalculateForMatch(Match match) {
        if (match.getHomeScore() == null || match.getAwayScore() == null) {
            return;
        }

        for (Prediction prediction : predictionRepository.findByMatchId(match.getId())) {
            int points = scoringService.calculatePoints(
                    prediction.getPredictedHomeScore(),
                    prediction.getPredictedAwayScore(),
                    match.getHomeScore(),
                    match.getAwayScore());
            prediction.setPointsEarned(points);
        }
    }

    private PredictionResponse toResponse(Prediction prediction) {
        Match match = prediction.getMatch();
        return new PredictionResponse(
                prediction.getId(),
                match.getId(),
                match.getHomeTeam(),
                match.getAwayTeam(),
                match.getLocation(),
                match.getKickoffAt(),
                prediction.getPredictedHomeScore(),
                prediction.getPredictedAwayScore(),
                prediction.getPointsEarned(),
                match.getStatus(),
                match.getHomeScore(),
                match.getAwayScore());
    }
}
