package com.zebiso.Backend.dto;

import java.util.UUID;

public record RankingEntryResponse(int position, UUID userId, String name, String avatar, long totalPoints, Integer previousPosition) {
}
