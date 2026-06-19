package com.zebiso.Backend.repository;

import com.zebiso.Backend.model.Prediction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PredictionRepository extends JpaRepository<Prediction, UUID> {

    Optional<Prediction> findByUserIdAndMatchId(UUID userId, UUID matchId);

    List<Prediction> findByUserIdOrderByUpdatedAtDesc(UUID userId);

    List<Prediction> findByMatchId(UUID matchId);

    void deleteByUserId(UUID userId);

    @Query("""
            SELECT u.id AS userId, u.name AS name, u.avatar AS avatar, (COALESCE(SUM(p.pointsEarned), 0) + u.bonusPoints) AS totalPoints
            FROM User u
            LEFT JOIN Prediction p ON p.user = u
            WHERE u.hiddenFromRanking = false
            GROUP BY u.id, u.name, u.avatar, u.bonusPoints
            ORDER BY totalPoints DESC, u.name ASC
            """)
    List<RankingProjection> findRanking();
}
