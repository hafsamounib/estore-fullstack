import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink, Router } from '@angular/router';
import { AuthService } from '../../services/auth';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class LoginComponent {
  credentials = { email: '', password: '' };
  errorMsg = '';
  loading = false;

  constructor(private auth: AuthService, private router: Router) {}

  login() {
    this.loading = true;
    this.errorMsg = '';
    this.auth.login(this.credentials).subscribe({
      next: () => this.router.navigate(['/catalog']),
      error: (err) => {
        this.loading = false;
        this.errorMsg = err.error?.message || 'Invalid email or password';
      }
    });
  }
}
