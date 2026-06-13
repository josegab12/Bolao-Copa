package com.zebiso.Backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "matches")
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 80)
    private String homeTeam;

    @Column(nullable = false, length = 80)
    private String awayTeam;

    @Column(nullable = false)
    private LocalDateTime kickoffAt;

    @Column(length = 60)
    private String stage;

    @Column(length = 10)
    private String groupName;

    @Column(length = 100)
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MatchStatus status = MatchStatus.AGENDADO;

    private Integer homeScore;

    private Integer awayScore;

    protected Match() {
    }

    public Match(String homeTeam, String awayTeam, LocalDateTime kickoffAt, String stage, String groupName, String location) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.kickoffAt = kickoffAt;
        this.stage = stage;
        this.groupName = groupName;
        this.location = location;
    }

    public boolean isFinished() {
        return status == MatchStatus.FINALIZADO;
    }

    public boolean hasStarted() {
        return status != MatchStatus.AGENDADO || kickoffAt.isBefore(LocalDateTime.now());
    }

    public UUID getId() {
        return id;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public LocalDateTime getKickoffAt() {
        return kickoffAt;
    }

    public String getStage() {
        return stage;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getLocation() {
        return location;
    }

    public MatchStatus getStatus() {
        return status;
    }

    public void setStatus(MatchStatus status) {
        this.status = status;
    }

    public Integer getHomeScore() {
        return homeScore;
    }

    public void setHomeScore(Integer homeScore) {
        this.homeScore = homeScore;
    }

    public Integer getAwayScore() {
        return awayScore;
    }

    public void setAwayScore(Integer awayScore) {
        this.awayScore = awayScore;
    }
}
