import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class ProductService {
  private api = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

  getProducts(search?: string, categoryId?: number | null): Observable<any[]> {
    let params = new HttpParams();

    if (search && search.trim() !== '') {
      params = params.set('search', search.trim());
    }

    if (categoryId !== null && categoryId !== undefined) {
      params = params.set('categoryId', categoryId.toString());
    }

    console.log('REQUEST URL:', `${this.api}/products`);
    console.log('REQUEST PARAMS:', params.toString());

    return this.http.get<any[]>(`${this.api}/products`, { params });
  }

  getProduct(id: number): Observable<any> {
    return this.http.get<any>(`${this.api}/products/${id}`);
  }

  getCategories(): Observable<any[]> {
    return this.http.get<any[]>(`${this.api}/categories`);
  }

  getReviews(productId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.api}/reviews/product/${productId}`);
  }

  addReview(review: any): Observable<any> {
    return this.http.post<any>(`${this.api}/reviews`, review);
  }
}