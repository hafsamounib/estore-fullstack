import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { tap } from 'rxjs/operators';
import { AuthService } from './auth';

@Injectable({ providedIn: 'root' })
export class CartService {
  private api = 'http://localhost:8080/api/cart';
  private cartCount$ = new BehaviorSubject<number>(0);
  cartCount = this.cartCount$.asObservable();

  constructor(private http: HttpClient, private auth: AuthService) {}

  getCart(): Observable<any> {
    const userId = this.auth.getUser()?.id;
    return this.http.get<any>(`${this.api}/${userId}`).pipe(
      tap((cart: any) => this.cartCount$.next(cart.items?.length || 0))
    );
  }

  addToCart(productId: number, quantity: number): Observable<any> {
    const userId = this.auth.getUser()?.id;
    return this.http.post<any>(`${this.api}/add`, { userId, productId, quantity }).pipe(
      tap((cart: any) => this.cartCount$.next(cart.items?.length || 0))
    );
  }

  updateItem(itemId: number, quantity: number): Observable<any> {
    const userId = this.auth.getUser()?.id;
    return this.http.put<any>(`${this.api}/update`, { userId, itemId, quantity }).pipe(
      tap((cart: any) => this.cartCount$.next(cart.items?.length || 0))
    );
  }

  removeItem(itemId: number): Observable<any> {
    const userId = this.auth.getUser()?.id;
    return this.http.delete<any>(`${this.api}/remove/${userId}/${itemId}`).pipe(
      tap((cart: any) => this.cartCount$.next(cart.items?.length || 0))
    );
  }
}
