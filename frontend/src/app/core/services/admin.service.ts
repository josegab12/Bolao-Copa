import { Injectable, signal } from '@angular/core';
import { environment } from '../../../environments/environment';

const STORAGE_KEY = 'bolao_admin';

@Injectable({ providedIn: 'root' })
export class AdminService {
  readonly isAdmin = signal(this.load());

  unlock(pin: string): boolean {
    if (pin.trim() === environment.adminPin) {
      sessionStorage.setItem(STORAGE_KEY, '1');
      this.isAdmin.set(true);
      return true;
    }
    return false;
  }

  logout(): void {
    sessionStorage.removeItem(STORAGE_KEY);
    this.isAdmin.set(false);
  }

  private load(): boolean {
    return sessionStorage.getItem(STORAGE_KEY) === '1';
  }
}
