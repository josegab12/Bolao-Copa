package com.zebiso.Backend.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateProfileRequest(@NotBlank String avatar) {
}
