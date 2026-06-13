package com.zebiso.Backend.dto;

import java.time.LocalDate;
import java.util.List;

public record DayMatchesResponse(LocalDate date, int matchCount, List<MatchResponse> matches) {
}
