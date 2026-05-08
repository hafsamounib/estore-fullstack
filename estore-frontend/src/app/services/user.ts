import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from './auth';

@Injectable({ providedIn: 'root' })
export class UserService {
  private api = 'http://localhost:8080/api/users';
  private profileApi = 'http://localhost:8080/api/profile';

  constructor(private http: HttpClient, private auth: AuthService) {}

  getProfile(id: number): Observable<any> {
    return this.http.get<any>(`${this.api}/${id}`);
  }

  updateProfile(id: number, data: { name: string }): Observable<any> {
    return this.http.put<any>(`${this.api}/${id}`, data);
  }

  getExtendedProfile(id: number): Observable<any> {
    return this.http.get<any>(`${this.profileApi}/${id}`);
  }

  updateExtendedProfile(id: number, data: { phone: string; address: string; city: string; country: string }): Observable<any> {
    return this.http.put<any>(`${this.profileApi}/${id}`, data);
  }
}