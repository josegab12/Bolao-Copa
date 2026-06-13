import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';
import { ShellComponent } from './layout/shell/shell.component';
import { LoginComponent } from './pages/login/login.component';
import { JogosComponent } from './pages/jogos/jogos.component';
import { PalpitesComponent } from './pages/palpites/palpites.component';
import { RankingComponent } from './pages/ranking/ranking.component';
import { AdminComponent } from './pages/admin/admin.component';

export const routes: Routes = [
  {
    path: '',
    component: ShellComponent,
    children: [
      { path: '', pathMatch: 'full', redirectTo: 'jogos' },
      { path: 'login', component: LoginComponent },
      { path: 'jogos', component: JogosComponent, canActivate: [authGuard] },
      { path: 'palpites', component: PalpitesComponent, canActivate: [authGuard] },
      { path: 'ranking', component: RankingComponent, canActivate: [authGuard] },
      { path: 'admin', component: AdminComponent }
    ]
  },
  { path: '**', redirectTo: 'jogos' }
];
