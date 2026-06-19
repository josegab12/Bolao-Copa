package com.zebiso.Backend.dto;

import java.util.UUID;

public record UserResponse(UUID id, String name, String avatar, boolean hiddenFromRanking, int bonusPoints) {
}
