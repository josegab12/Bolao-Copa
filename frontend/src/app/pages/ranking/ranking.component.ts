import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RankingService } from '../../core/services/ranking.service';
import { AuthService } from '../../core/services/auth.service';
import { RankingEntry } from '../../core/models/bolao.models';

@Component({
  selector: 'app-ranking',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './ranking.component.html',
  styleUrl: './ranking.component.scss'
})
export class RankingComponent implements OnInit {
  private readonly rankingService = inject(RankingService);
  readonly auth = inject(AuthService);

  ranking = signal<RankingEntry[]>([]);
  loading = signal(true);
  error = signal('');

  ngOnInit(): void {
    this.rankingService.list().subscribe({
      next: (ranking) => {
        this.ranking.set(ranking);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
        this.error.set('Nao foi possivel carregar o ranking.');
      }
    });
  }

  isCurrentUser(entry: RankingEntry): boolean {
    return entry.userId === this.auth.getUserId();
  }

  getTrend(entry: RankingEntry): 'up' | 'down' | 'stable' {
    if (entry.previousPosition === null || entry.previousPosition === undefined) {
      return 'stable';
    }
    if (entry.previousPosition > entry.position) {
      return 'up';
    }
    if (entry.previousPosition < entry.position) {
      return 'down';
    }
    return 'stable';
  }

  getTrendDescription(entry: RankingEntry): string {
    const trend = this.getTrend(entry);
    if (trend === 'up') return 'Subiu de posição';
    if (trend === 'down') return 'Desceu de posição';
    return 'Manteve a posição';
  }
}
