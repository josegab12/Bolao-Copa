package com.zebiso.Backend.service;

import com.zebiso.Backend.config.WebConfig.BolaoProperties;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ScoringServiceTest {

    private final ScoringService scoringService = new ScoringService(new BolaoProperties(5, 2));

    @Test
    void exactScoreGivesMaxPoints() {
        assertEquals(5, scoringService.calculatePoints(2, 1, 2, 1));
    }

    @Test
    void correctWinnerGivesPartialPoints() {
        assertEquals(2, scoringService.calculatePoints(3, 0, 1, 0));
    }

    @Test
    void correctDrawGivesPartialPoints() {
        assertEquals(2, scoringService.calculatePoints(1, 1, 0, 0));
    }

    @Test
    void wrongOutcomeGivesZeroPoints() {
        assertEquals(0, scoringService.calculatePoints(2, 0, 0, 1));
    }
}
