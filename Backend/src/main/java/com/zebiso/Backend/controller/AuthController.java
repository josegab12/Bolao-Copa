package com.zebiso.Backend.controller;

import com.zebiso.Backend.dto.EnterRequest;
import com.zebiso.Backend.dto.UpdateProfileRequest;
import com.zebiso.Backend.dto.UserResponse;
import com.zebiso.Backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/entrar")
    public ResponseEntity<UserResponse> enter(@Valid @RequestBody EnterRequest request) {
        return ResponseEntity.ok(userService.enter(request.name()));
    }

    @PatchMapping("/perfil/{id}")
    public ResponseEntity<UserResponse> updateProfile(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateProfileRequest request) {
        return ResponseEntity.ok(userService.updateAvatar(id, request.avatar()));
    }
}
