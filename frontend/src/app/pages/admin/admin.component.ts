import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatchService } from '../../core/services/match.service';
import { AdminService } from '../../core/services/admin.service';
import { DayMatches, Match } from '../../core/models/bolao.models';
import { HttpErrorResponse } from '@angular/common/http';

interface ResultForm {
  homeScore: number;
  awayScore: number;
  saving: boolean;
  success: string;
  error: string;
}

@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin.component.html',
  styleUrl: './admin.component.scss'
})
export class AdminComponent implements OnInit {
  private readonly matchService = inject(MatchService);
  readonly admin = inject(AdminService);

  pin = '';
  pinError = signal('');
  days = signal<DayMatches[]>([]);
  loading = signal(false);
  pageError = signal('');
  forms = signal<Record<string, ResultForm>>({});

  ngOnInit(): void {
    if (this.admin.isAdmin()) {
      this.loadMatches();
    }
  }

  unlock(): void {
    this.pinError.set('');
    if (!this.admin.unlock(this.pin)) {
      this.pinError.set('Senha de admin invalida.');
      return;
    }
    this.pin = '';
    this.loadMatches();
  }

  logoutAdmin(): void {
    this.admin.logout();
    this.days.set([]);
    this.forms.set({});
  }

  loadMatches(): void {
    this.loading.set(true);
    this.pageError.set('');

    this.matchService.listGroupedByDay().subscribe({
      next: (days) => {
        this.days.set(days);
        this.initForms(days);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
        this.pageError.set('Nao foi possivel carregar os jogos.');
      }
    });
  }

  formatDate(date: string): string {
    return new Date(date + 'T12:00:00').toLocaleDateString('pt-BR', {
      weekday: 'long',
      day: '2-digit',
      month: 'long'
    });
  }

  formatTime(kickoffAt: string): string {
    return new Date(kickoffAt).toLocaleTimeString('pt-BR', {
      hour: '2-digit',
      minute: '2-digit'
    });
  }

  statusLabel(status: Match['status']): string {
    switch (status) {
      case 'FINALIZADO':
        return 'Finalizado';
      case 'EM_ANDAMENTO':
        return 'Em andamento';
      default:
        return 'Agendado';
    }
  }

  getForm(matchId: string): ResultForm {
    return this.forms()[matchId] ?? {
      homeScore: 0,
      awayScore: 0,
      saving: false,
      success: '',
      error: ''
    };
  }

  updateForm(matchId: string, patch: Partial<ResultForm>): void {
    this.forms.update((current) => ({
      ...current,
      [matchId]: { ...this.getForm(matchId), ...patch, success: '', error: '' }
    }));
  }

  saveResult(match: Match): void {
    const form = this.getForm(match.id);
    this.updateForm(match.id, { saving: true, success: '', error: '' });

    this.matchService.registerResult(match.id, form.homeScore, form.awayScore).subscribe({
      next: (updated) => {
        this.updateForm(match.id, {
          saving: false,
          success: 'Resultado registrado. Pontos recalculados.'
        });
        this.patchMatch(updated);
      },
      error: (err: HttpErrorResponse) => {
        this.updateForm(match.id, {
          saving: false,
          error: err.error?.message ?? 'Erro ao registrar resultado.'
        });
      }
    });
  }

  private initForms(days: DayMatches[]): void {
    const nextForms: Record<string, ResultForm> = {};

    for (const day of days) {
      for (const match of day.matches) {
        nextForms[match.id] = {
          homeScore: match.homeScore ?? 0,
          awayScore: match.awayScore ?? 0,
          saving: false,
          success: '',
          error: ''
        };
      }
    }

    this.forms.set(nextForms);
  }

  private patchMatch(updated: Match): void {
    this.days.update((days) =>
      days.map((day) => ({
        ...day,
        matches: day.matches.map((match) => (match.id === updated.id ? updated : match))
      }))
    );
  }
}
