
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { AuthService } from '../../services/auth';
import { UserService } from '../../services/user';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './profile.html',
  styleUrl: './profile.css'
})
export class ProfileComponent implements OnInit {
  user: any = null;
  profile: any = null;
  editMode = false;
  editName = '';
  profileForm = { phone: '', address: '', city: '', country: '' };
  successMsg = '';
  errorMsg = '';
  loading = false;

  constructor(public auth: AuthService, private userService: UserService, private http: HttpClient) {}

  ngOnInit() {
    this.user = this.auth.getUser();
    this.editName = this.user?.name || '';

    if (this.user?.id) {
      this.userService.getProfile(this.user.id).subscribe({
        next: (data: any) => {
          this.user = data;
          this.editName = data.name;
          this.auth.saveUser(data);
        },
        error: () => {}
      });

      this.userService.getExtendedProfile(this.user.id).subscribe({
        next: (data: any) => {
          this.profile = data;
          this.profileForm = {
            phone: data.phone || '',
            address: data.address || '',
            city: data.city || '',
            country: data.country || ''
          };
        },
        error: () => {}
      });
    }
  }

  saveProfile() {
    const userId = this.user?.id;
    if (!userId) {
      this.errorMsg = 'Utilisateur non connecté';
      return;
    }

    const body = {
      phone:   this.profileForm.phone   || '',
      address: this.profileForm.address || '',
      city:    this.profileForm.city    || '',
      country: this.profileForm.country || ''
    };

    this.http.put(
      `http://localhost:8080/api/profile/${userId}`,
      body,
      { headers: { 'Content-Type': 'application/json' } }
    ).subscribe({
      next: (data: any) => {
        this.profile = data;
        this.profileForm = {
          phone:   data.phone   || '',
          address: data.address || '',
          city:    data.city    || '',
          country: data.country || ''
        };
        this.successMsg = '✅ Profil mis à jour !';
        this.editMode = false;
        setTimeout(() => this.successMsg = '', 3000);
      },
      error: (err) => {
        console.error('Save error:', err);
        this.errorMsg = '❌ Erreur lors de la sauvegarde';
        setTimeout(() => this.errorMsg = '', 3000);
      }
    });
  }

  logout() {
    this.auth.logout();
  }
}
