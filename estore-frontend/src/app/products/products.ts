import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { ProductService } from '../services/product';
import { CartService } from '../services/cart';
import { AuthService } from '../services/auth';

@Component({
  selector: 'app-products',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './products.html',
  styleUrl: './products.css'
})
export class ProductsComponent implements OnInit {
  product: any = null;
  reviews: any[] = [];
  loading = true;
  quantity = 1;
  added = false;

  newReview = { rating: 5, comment: '' };
  reviewSubmitted = false;

  constructor(
    private route: ActivatedRoute,
    private productService: ProductService,
    private cartService: CartService,
    public auth: AuthService
  ) {}

  ngOnInit() {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.productService.getProduct(id).subscribe({
      next: (p: any) => { this.product = p; this.loading = false; },
      error: () => this.loading = false
    });
    this.productService.getReviews(id).subscribe((r: any[]) => this.reviews = r);
  }

  addToCart() {
    this.cartService.addToCart(this.product.id, this.quantity).subscribe(() => {
      this.added = true;
      setTimeout(() => this.added = false, 2000);
    });
  }

  submitReview() {
    const user = this.auth.getUser();
    this.productService.addReview({
      productId: this.product.id,
      userId: user.id,
      userName: user.name,
      rating: this.newReview.rating,
      comment: this.newReview.comment
    }).subscribe((r: any) => {
      this.reviews = [r, ...this.reviews];
      this.newReview = { rating: 5, comment: '' };
      this.reviewSubmitted = true;
      setTimeout(() => this.reviewSubmitted = false, 2000);
    });
  }

  decreaseQty() { if (this.quantity > 1) this.quantity--; }
  increaseQty() { if (this.quantity < this.product?.inventory?.quantity) this.quantity++; }

  get stars() { return [1, 2, 3, 4, 5]; }

  get avgRating() {
    if (!this.reviews.length) return 0;
    return this.reviews.reduce((s: number, r: any) => s + r.rating, 0) / this.reviews.length;
  }
}