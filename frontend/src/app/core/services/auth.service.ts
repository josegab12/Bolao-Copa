import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { tap } from 'rxjs/operators';
import { environment } from '../../../environments/environment';
import { User } from '../models/bolao.models';

const STORAGE_KEY = 'bolao_user';

@Injectable({ providedIn: 'root' })
export class AuthService {
  readonly currentUser = signal<User | null>(this.loadUser());

  constructor(
    private readonly http: HttpClient,
    private readonly router: Router
  ) {}

  enter(name: string) {
    return this.http.post<User>(`${environment.apiUrl}/auth/entrar`, { name }).pipe(
      tap((user) => {
        this.saveUser(user);
      })
    );
  }

  updateProfile(avatar: string) {
    const userId = this.getUserId();
    if (!userId) throw new Error('Usuario nao autenticado');

    return this.http.patch<User>(`${environment.apiUrl}/auth/perfil/${userId}`, { avatar }).pipe(
      tap((user) => {
        this.saveUser(user);
      })
    );
  }

  private saveUser(user: User): void {
    localStorage.setItem(STORAGE_KEY, JSON.stringify(user));
    this.currentUser.set(user);
  }

  logout(): void {
    localStorage.removeItem(STORAGE_KEY);
    this.currentUser.set(null);
    this.router.navigate(['/login']);
  }

  isLoggedIn(): boolean {
    return this.currentUser() !== null;
  }

  getUserId(): string | null {
    return this.currentUser()?.id ?? null;
  }

  private loadUser(): User | null {
    const raw = localStorage.getItem(STORAGE_KEY);
    if (!raw) {
      return null;
    }

    try {
      return JSON.parse(raw) as User;
    } catch {
      localStorage.removeItem(STORAGE_KEY);
      return null;
    }
  }
}
