import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { PredictionService } from '../../core/services/prediction.service';
import { Prediction } from '../../core/models/bolao.models';

@Component({
  selector: 'app-palpites',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './palpites.component.html',
  styleUrl: './palpites.component.scss'
})
export class PalpitesComponent implements OnInit {
  private readonly predictionService = inject(PredictionService);

  predictions = signal<Prediction[]>([]);
  loading = signal(true);
  error = signal('');

  ngOnInit(): void {
    this.predictionService.listMine().subscribe({
      next: (predictions) => {
        this.predictions.set(predictions);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
        this.error.set('Nao foi possivel carregar seus palpites.');
      }
    });
  }

  formatDateTime(value: string): string {
    return new Date(value).toLocaleString('pt-BR', {
      day: '2-digit',
      month: '2-digit',
      hour: '2-digit',
      minute: '2-digit'
    });
  }

  totalPoints(): number {
    return this.predictions().reduce((sum, item) => sum + item.pointsEarned, 0);
  }
}
