import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatchService } from '../../core/services/match.service';
import { AdminService } from '../../core/services/admin.service';
import { UserAdminService } from '../../core/services/user-admin.service';
import { DayMatches, Match, User } from '../../core/models/bolao.models';
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
  private readonly userAdminService = inject(UserAdminService);
  readonly admin = inject(AdminService);

  pin = '';
  pinError = signal('');
  days = signal<DayMatches[]>([]);
  users = signal<User[]>([]);
  loading = signal(false);
  loadingUsers = signal(false);
  pageError = signal('');
  forms = signal<Record<string, ResultForm>>({});

  activeTab = signal<'jogos' | 'usuarios' | 'criar-jogo'>('jogos');

  // Form fields for creating a new match
  newHomeTeam = '';
  newAwayTeam = '';
  newDate = '';
  newTime = '';
  newStage = 'Eliminatórias';
  newGroupName = '';
  newLocation = '';
  creatingMatch = signal(false);
  createMatchSuccess = signal('');
  createMatchError = signal('');

  ngOnInit(): void {
    if (this.admin.isAdmin()) {
      this.loadData();
    }
  }

  unlock(): void {
    this.pinError.set('');
    if (!this.admin.unlock(this.pin)) {
      this.pinError.set('Senha de admin invalida.');
      return;
    }
    this.pin = '';
    this.loadData();
  }

  logoutAdmin(): void {
    this.admin.logout();
    this.days.set([]);
    this.users.set([]);
    this.forms.set({});
  }

  loadData(): void {
    this.loadMatches();
    this.loadUsers();
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

  loadUsers(): void {
    this.loadingUsers.set(true);
    this.userAdminService.listAll().subscribe({
      next: (users) => {
        this.users.set(users);
        this.loadingUsers.set(false);
      },
      error: () => {
        this.loadingUsers.set(false);
      }
    });
  }

  toggleUserVisibility(user: User): void {
    this.userAdminService.toggleVisibility(user.id).subscribe({
      next: (updated) => {
        this.users.update(users => users.map(u => u.id === updated.id ? updated : u));
      }
    });
  }

  resetUserPoints(user: User): void {
    if (confirm(`Deseja realmente zerar todos os pontos de "${user.name}"? Isso apagara todos os seus palpites.`)) {
      this.userAdminService.resetPoints(user.id).subscribe({
        next: () => {
          alert(`Pontos de ${user.name} foram zerados.`);
          this.loadUsers();
        }
      });
    }
  }

  adjustPoints(user: User, points: number): void {
    if (isNaN(points) || points === 0) return;
    this.userAdminService.addPoints(user.id, points).subscribe({
      next: (updated) => {
        this.users.update(users => users.map(u => u.id === updated.id ? updated : u));
        alert(`Pontos do usuário "${user.name}" ajustados em ${points > 0 ? '+' : ''}${points}. Novo saldo de pontos extras: ${updated.bonusPoints ?? 0}.`);
      },
      error: (err: HttpErrorResponse) => {
        alert(err.error?.message ?? 'Erro ao ajustar pontos.');
      }
    });
  }

  createMatch(): void {
    this.createMatchSuccess.set('');
    this.createMatchError.set('');

    if (!this.newHomeTeam.trim() || !this.newAwayTeam.trim() || !this.newDate || !this.newTime) {
      this.createMatchError.set('Preencha os campos obrigatórios (Times, Dia e Horário).');
      return;
    }

    this.creatingMatch.set(true);

    const kickoffAt = `${this.newDate}T${this.newTime}:00`;

    this.matchService.createMatch({
      homeTeam: this.newHomeTeam.trim(),
      awayTeam: this.newAwayTeam.trim(),
      kickoffAt,
      stage: this.newStage.trim() || undefined,
      groupName: this.newGroupName.trim() || undefined,
      location: this.newLocation.trim() || undefined
    }).subscribe({
      next: () => {
        this.creatingMatch.set(false);
        this.createMatchSuccess.set('Partida criada com sucesso!');
        this.newHomeTeam = '';
        this.newAwayTeam = '';
        this.newDate = '';
        this.newTime = '';
        this.newStage = 'Eliminatórias';
        this.newGroupName = '';
        this.newLocation = '';
        this.loadMatches();
      },
      error: (err: HttpErrorResponse) => {
        this.creatingMatch.set(false);
        this.createMatchError.set(err.error?.message ?? 'Erro ao criar partida.');
      }
    });
  }

  deleteMatch(match: Match): void {
    if (confirm(`Deseja realmente apagar a partida "${match.homeTeam} x ${match.awayTeam}"? Isso tambem apagara todos os palpites associados a ela.`)) {
      this.matchService.deleteMatch(match.id).subscribe({
        next: () => {
          alert('Partida apagada com sucesso.');
          this.loadMatches();
        },
        error: (err: HttpErrorResponse) => {
          alert(err.error?.message ?? 'Erro ao apagar partida.');
        }
      });
    }
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

  isToday(date: string): boolean {
    const today = new Date().toISOString().split('T')[0];
    return date === today;
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
