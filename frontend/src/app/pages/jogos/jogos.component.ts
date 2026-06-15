import { Component, inject, OnInit, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatchService } from '../../core/services/match.service';
import { PredictionService } from '../../core/services/prediction.service';
import { DayMatches, Match, Prediction, ScoringRules, MatchPrediction } from '../../core/models/bolao.models';
import { HttpErrorResponse } from '@angular/common/http';

interface MatchForm {
  homeScore: number;
  awayScore: number;
  saving: boolean;
  saved: boolean;
  error: string;
}

@Component({
  selector: 'app-jogos',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './jogos.component.html',
  styleUrl: './jogos.component.scss'
})
export class JogosComponent implements OnInit {
  private readonly matchService = inject(MatchService);
  private readonly predictionService = inject(PredictionService);

  days = signal<DayMatches[]>([]);
  rules = signal<ScoringRules | null>(null);
  loading = signal(true);
  error = signal('');
  forms = signal<Record<string, MatchForm>>({});

  // Ver palpites dos outros
  selectedMatch = signal<Match | null>(null);
  matchPredictions = signal<MatchPrediction[]>([]);
  loadingMatchPredictions = signal(false);

  todayDay = computed(() => this.days().find(d => this.isToday(d.date)));
  otherDays = computed(() => this.days().filter(d => !this.isToday(d.date)));

  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {
    this.loading.set(true);
    this.error.set('');

    this.matchService.listGroupedByDay().subscribe({
      next: (days) => {
        this.days.set(days);
        this.loading.set(false);
        this.loadPredictions();
      },
      error: () => {
        this.loading.set(false);
        this.error.set('Nao foi possivel carregar os jogos.');
      }
    });

    this.predictionService.getScoringRules().subscribe({
      next: (rules) => this.rules.set(rules)
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

  isToday(date: string): boolean {
    const today = new Date();
    const year = today.getFullYear();
    const month = String(today.getMonth() + 1).padStart(2, '0');
    const day = String(today.getDate()).padStart(2, '0');
    const localDate = `${year}-${month}-${day}`;
    return date === localDate;
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

  canPredict(match: Match): boolean {
    return match.status === 'AGENDADO' && new Date(match.kickoffAt) > new Date();
  }

  getForm(matchId: string): MatchForm {
    return this.forms()[matchId] ?? {
      homeScore: 0,
      awayScore: 0,
      saving: false,
      saved: false,
      error: ''
    };
  }

  updateForm(matchId: string, patch: Partial<MatchForm>): void {
    this.forms.update((current) => ({
      ...current,
      [matchId]: { ...this.getForm(matchId), ...patch }
    }));
  }

  savePrediction(match: Match): void {
    const form = this.getForm(match.id);
    this.updateForm(match.id, { saving: true, saved: false, error: '' });

    this.predictionService.save(match.id, form.homeScore, form.awayScore).subscribe({
      next: () => {
        this.updateForm(match.id, { saving: false, saved: true });
      },
      error: (err: HttpErrorResponse) => {
        this.updateForm(match.id, {
          saving: false,
          saved: false,
          error: err.error?.message ?? 'Erro ao salvar palpite.'
        });
      }
    });
  }

  viewOthers(match: Match): void {
    this.selectedMatch.set(match);
    this.loadingMatchPredictions.set(true);
    this.matchPredictions.set([]);

    this.predictionService.listByMatch(match.id).subscribe({
      next: (preds) => {
        this.matchPredictions.set(preds);
        this.loadingMatchPredictions.set(false);
      },
      error: () => {
        this.loadingMatchPredictions.set(false);
      }
    });
  }

  closeModal(): void {
    this.selectedMatch.set(null);
    this.matchPredictions.set([]);
  }

  private loadPredictions(): void {
    this.predictionService.listMine().subscribe({
      next: (predictions) => {
        const nextForms: Record<string, MatchForm> = {};
        for (const prediction of predictions) {
          nextForms[prediction.matchId] = {
            homeScore: prediction.predictedHomeScore,
            awayScore: prediction.predictedAwayScore,
            saving: false,
            saved: true,
            error: ''
          };
        }
        this.forms.set(nextForms);
      }
    });
  }
}
