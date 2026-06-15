import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { Prediction, ScoringRules, MatchPrediction } from '../models/bolao.models';

@Injectable({ providedIn: 'root' })
export class PredictionService {
  constructor(private readonly http: HttpClient) {}

  save(matchId: string, homeScore: number, awayScore: number) {
    return this.http.post<Prediction>(`${environment.apiUrl}/palpites`, {
      matchId,
      homeScore,
      awayScore
    });
  }

  listMine() {
    return this.http.get<Prediction[]>(`${environment.apiUrl}/palpites/meus`);
  }

  listByMatch(matchId: string) {
    return this.http.get<MatchPrediction[]>(`${environment.apiUrl}/jogos/${matchId}/palpites`);
  }

  getScoringRules() {
    return this.http.get<ScoringRules>(`${environment.apiUrl}/pontuacao/regras`);
  }
}
