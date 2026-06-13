import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  private readonly auth = inject(AuthService);
  private readonly router = inject(Router);

  name = '';
  loading = signal(false);
  error = signal('');

  submit(): void {
    const trimmed = this.name.trim();
    if (trimmed.length < 2) {
      this.error.set('Digite um nome com pelo menos 2 caracteres.');
      return;
    }

    this.loading.set(true);
    this.error.set('');

    this.auth.enter(trimmed).subscribe({
      next: () => {
        this.loading.set(false);
        this.router.navigate(['/jogos']);
      },
      error: (err: HttpErrorResponse) => {
        this.loading.set(false);
        this.error.set(err.error?.message ?? 'Nao foi possivel entrar. Tente novamente.');
      }
    });
  }
}
