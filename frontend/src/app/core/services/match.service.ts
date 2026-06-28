import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { DayMatches, Match } from '../models/bolao.models';

@Injectable({ providedIn: 'root' })
export class MatchService {
  constructor(private readonly http: HttpClient) {}

  listGroupedByDay() {
    return this.http.get<DayMatches[]>(`${environment.apiUrl}/jogos/por-dia`);
  }

  listByDay(date: string) {
    const params = new HttpParams().set('data', date);
    return this.http.get<DayMatches>(`${environment.apiUrl}/jogos/dia`, { params });
  }

  listAll() {
    return this.http.get<Match[]>(`${environment.apiUrl}/jogos`);
  }

  registerResult(matchId: string, homeScore: number, awayScore: number) {
    return this.http.patch<Match>(`${environment.apiUrl}/jogos/${matchId}/resultado`, {
      homeScore,
      awayScore
    });
  }

  createMatch(match: { homeTeam: string; awayTeam: string; kickoffAt: string; stage?: string; groupName?: string; location?: string }) {
    return this.http.post<Match>(`${environment.apiUrl}/jogos`, match);
  }
}
