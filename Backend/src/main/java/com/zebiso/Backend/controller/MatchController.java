package com.zebiso.Backend.controller;

import com.zebiso.Backend.dto.CreateMatchRequest;
import com.zebiso.Backend.dto.DayMatchesResponse;
import com.zebiso.Backend.dto.MatchResponse;
import com.zebiso.Backend.dto.MatchResultRequest;
import com.zebiso.Backend.service.MatchService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/jogos")
public class MatchController {

    private final MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @GetMapping
    public ResponseEntity<List<MatchResponse>> listAll() {
        return ResponseEntity.ok(matchService.listAll());
    }

    @GetMapping("/por-dia")
    public ResponseEntity<List<DayMatchesResponse>> listGroupedByDay() {
        return ResponseEntity.ok(matchService.listGroupedByDay());
    }

    @GetMapping("/dia")
    public ResponseEntity<DayMatchesResponse> listByDay(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        return ResponseEntity.ok(matchService.listByDay(data));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MatchResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(matchService.findById(id));
    }

    @PatchMapping("/{id}/resultado")
    public ResponseEntity<MatchResponse> registerResult(
            @PathVariable UUID id,
            @Valid @RequestBody MatchResultRequest request) {
        return ResponseEntity.ok(matchService.registerResult(id, request));
    }

    @PostMapping
    public ResponseEntity<MatchResponse> createMatch(
            @Valid @RequestBody CreateMatchRequest request) {
        return ResponseEntity.ok(matchService.createMatch(request));
    }
}
