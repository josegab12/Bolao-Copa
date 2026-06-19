import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { User } from '../models/bolao.models';

@Injectable({ providedIn: 'root' })
export class UserAdminService {
  private readonly baseUrl = `${environment.apiUrl}/admin/usuarios`;

  constructor(private readonly http: HttpClient) {}

  listAll() {
    return this.http.get<User[]>(this.baseUrl);
  }

  toggleVisibility(userId: string) {
    return this.http.patch<User>(`${this.baseUrl}/${userId}/visibilidade-ranking`, {});
  }

  resetPoints(userId: string) {
    return this.http.patch<void>(`${this.baseUrl}/${userId}/reset-pontos`, {});
  }

  addPoints(userId: string, points: number) {
    return this.http.patch<User>(`${this.baseUrl}/${userId}/adicionar-pontos`, { points });
  }
}
