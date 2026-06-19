package com.zebiso.Backend.repository;

import java.util.UUID;

public interface RankingProjection {

    UUID getUserId();

    String getName();

    String getAvatar();

    Long getTotalPoints();

    Integer getPreviousPosition();

    Integer getCurrentPosition();
}
