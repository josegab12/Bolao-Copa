package com.zebiso.Backend.service;

import com.zebiso.Backend.config.WebConfig.BolaoProperties;
import org.springframework.stereotype.Service;

@Service
public class ScoringService {

    private final BolaoProperties properties;

    public ScoringService(BolaoProperties properties) {
        this.properties = properties;
    }

    public int calculatePoints(int predictedHome, int predictedAway, int actualHome, int actualAway) {
        if (predictedHome == actualHome && predictedAway == actualAway) {
            return properties.pontosResultadoExato();
        }

        if (outcome(predictedHome, predictedAway) == outcome(actualHome, actualAway)) {
            return properties.pontosVencedor();
        }

        return 0;
    }

    private int outcome(int home, int away) {
        return Integer.compare(home, away);
    }
}
