import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink, Router } from '@angular/router';
import { AuthService } from '../../services/auth';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './register.html',
  styleUrl: './register.css'
})
export class RegisterComponent {
  data = { name: '', email: '', password: '', confirmPassword: '' };
  errorMsg = '';
  loading = false;

  constructor(private auth: AuthService, private router: Router) {}

  register() {
    if (this.data.password !== this.data.confirmPassword) {
      this.errorMsg = 'Passwords do not match';
      return;
    }
    this.loading = true;
    this.errorMsg = '';
    this.auth.register({
      name: this.data.name,
      email: this.data.email,
      password: this.data.password
    }).subscribe({
      next: () => this.router.navigate(['/catalog']),
      error: (err) => {
        this.loading = false;
        this.errorMsg = err.error?.message || 'Registration failed. Please try again.';
      }
    });
  }
}