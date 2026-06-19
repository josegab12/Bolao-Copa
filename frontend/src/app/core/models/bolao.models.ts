export interface User {
  id: string;
  name: string;
  avatar?: string;
  hiddenFromRanking?: boolean;
  bonusPoints?: number;
}

export interface Match {
  id: string;
  homeTeam: string;
  awayTeam: string;
  kickoffAt: string;
  date: string;
  stage: string;
  groupName: string;
  location?: string;
  status: 'AGENDADO' | 'EM_ANDAMENTO' | 'FINALIZADO';
  homeScore: number | null;
  awayScore: number | null;
}

export interface DayMatches {
  date: string;
  matchCount: number;
  matches: Match[];
}

export interface Prediction {
  id: string;
  matchId: string;
  homeTeam: string;
  awayTeam: string;
  location?: string;
  kickoffAt: string;
  predictedHomeScore: number;
  predictedAwayScore: number;
  pointsEarned: number;
  matchStatus: Match['status'];
  actualHomeScore: number | null;
  actualAwayScore: number | null;
}

export interface RankingEntry {
  position: number;
  userId: string;
  name: string;
  avatar?: string;
  totalPoints: number;
  previousPosition?: number | null;
}

export interface MatchPrediction {
  id: string;
  userId: string;
  userName: string;
  userAvatar?: string;
  predictedHomeScore: number;
  predictedAwayScore: number;
  pointsEarned: number;
}

export interface ScoringRules {
  description: string;
  pontosResultadoExato: number;
  pontosVencedor: number;
}

export interface ApiError {
  message: string;
  timestamp: string;
}
