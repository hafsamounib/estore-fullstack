import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from './auth';

@Injectable({ providedIn: 'root' })
export class OrderService {
  private api = 'http://localhost:8080/api/orders';

  constructor(private http: HttpClient, private auth: AuthService) {}

  placeOrder(): Observable<any> {
    const userId = this.auth.getUser()?.id;
    return this.http.post<any>(this.api, { userId });
  }

  getMyOrders(): Observable<any[]> {
    const userId = this.auth.getUser()?.id;
    return this.http.get<any[]>(`${this.api}/user/${userId}`);
  }
}