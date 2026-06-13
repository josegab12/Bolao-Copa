import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { RankingEntry } from '../models/bolao.models';

@Injectable({ providedIn: 'root' })
export class RankingService {
  constructor(private readonly http: HttpClient) {}

  list() {
    return this.http.get<RankingEntry[]>(`${environment.apiUrl}/ranking`);
  }
}
