package com.zebiso.Backend.repository;

import com.zebiso.Backend.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface MatchRepository extends JpaRepository<Match, UUID> {

    List<Match> findAllByOrderByKickoffAtAsc();

    @Query("""
            SELECT m FROM Match m
            WHERE m.kickoffAt >= :start AND m.kickoffAt < :end
            ORDER BY m.kickoffAt ASC
            """)
    List<Match> findByDay(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
