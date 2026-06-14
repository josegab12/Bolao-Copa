package com.zebiso.Backend.repository;

import com.zebiso.Backend.model.Prediction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PredictionRepository extends JpaRepository<Prediction, UUID> {

    Optional<Prediction> findByUserIdAndMatchId(UUID userId, UUID matchId);

    List<Prediction> findByUserIdOrderByMatch_KickoffAtAsc(UUID userId);

    List<Prediction> findByMatchId(UUID matchId);

    void deleteByUserId(UUID userId);

    @Query("""
            SELECT p.user.id AS userId, p.user.name AS name, p.user.avatar AS avatar, COALESCE(SUM(p.pointsEarned), 0) AS totalPoints
            FROM Prediction p
            WHERE p.user.hiddenFromRanking = false
            GROUP BY p.user.id, p.user.name, p.user.avatar
            ORDER BY totalPoints DESC, name ASC
            """)
    List<RankingProjection> findRanking();
}
