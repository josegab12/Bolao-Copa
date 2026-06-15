package com.zebiso.Backend.controller;

import com.zebiso.Backend.dto.MatchPredictionResponse;
import com.zebiso.Backend.dto.PredictionRequest;
import com.zebiso.Backend.dto.PredictionResponse;
import com.zebiso.Backend.dto.RankingEntryResponse;
import com.zebiso.Backend.config.WebConfig.BolaoProperties;
import com.zebiso.Backend.service.PredictionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class PredictionController {

    private final PredictionService predictionService;
    private final BolaoProperties bolaoProperties;

    public PredictionController(PredictionService predictionService, BolaoProperties bolaoProperties) {
        this.predictionService = predictionService;
        this.bolaoProperties = bolaoProperties;
    }

    @PostMapping("/palpites")
    public ResponseEntity<PredictionResponse> save(
            @RequestHeader("X-User-Id") UUID userId,
            @Valid @RequestBody PredictionRequest request) {
        return ResponseEntity.ok(predictionService.save(userId, request));
    }

    @GetMapping("/palpites/meus")
    public ResponseEntity<List<PredictionResponse>> myPredictions(@RequestHeader("X-User-Id") UUID userId) {
        return ResponseEntity.ok(predictionService.listByUser(userId));
    }

    @GetMapping("/jogos/{matchId}/palpites")
    public ResponseEntity<List<MatchPredictionResponse>> matchPredictions(@PathVariable UUID matchId) {
        return ResponseEntity.ok(predictionService.listByMatch(matchId));
    }

    @GetMapping("/ranking")
    public ResponseEntity<List<RankingEntryResponse>> ranking() {
        return ResponseEntity.ok(predictionService.ranking());
    }

    @GetMapping("/pontuacao/regras")
    public ResponseEntity<ScoringRulesResponse> scoringRules() {
        return ResponseEntity.ok(new ScoringRulesResponse(
                "Resultado exato: pontuacao maxima. Acertar apenas o vencedor (ou empate): pontuacao parcial.",
                bolaoProperties.pontosResultadoExato(),
                bolaoProperties.pontosVencedor()));
    }

    public record ScoringRulesResponse(String description, int pontosResultadoExato, int pontosVencedor) {
    }
}
