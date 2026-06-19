package com.zebiso.Backend.controller;

import com.zebiso.Backend.dto.AddPointsRequest;
import com.zebiso.Backend.dto.UserResponse;
import com.zebiso.Backend.service.PredictionService;
import com.zebiso.Backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/usuarios")
public class AdminUserController {

    private final UserService userService;
    private final PredictionService predictionService;

    public AdminUserController(UserService userService, PredictionService predictionService) {
        this.userService = userService;
        this.predictionService = predictionService;
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> listAll() {
        return ResponseEntity.ok(userService.listAll());
    }

    @PatchMapping("/{id}/visibilidade-ranking")
    public ResponseEntity<UserResponse> toggleVisibility(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.toggleRankingVisibility(id));
    }

    @PatchMapping("/{id}/reset-pontos")
    public ResponseEntity<Void> resetPoints(@PathVariable UUID id) {
        predictionService.resetUserPoints(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/adicionar-pontos")
    public ResponseEntity<UserResponse> addPoints(
            @PathVariable UUID id,
            @RequestBody AddPointsRequest request) {
        UserResponse response = userService.addPoints(id, request.points());
        predictionService.updateRankingPositions();
        return ResponseEntity.ok(response);
    }
}
