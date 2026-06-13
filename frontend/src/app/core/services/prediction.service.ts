import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { Prediction, ScoringRules } from '../models/bolao.models';

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

  getScoringRules() {
    return this.http.get<ScoringRules>(`${environment.apiUrl}/pontuacao/regras`);
  }
}
