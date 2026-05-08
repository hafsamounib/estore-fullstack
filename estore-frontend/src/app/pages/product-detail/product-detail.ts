import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { ProductService } from '../../services/product';
import { CartService } from '../../services/cart';
import { AuthService } from '../../services/auth';

@Component({
  selector: 'app-product-detail',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './product-detail.html',
  styleUrl: './product-detail.css'
})
export class ProductDetailComponent implements OnInit {
  product: any = null;
  reviews: any[] = [];
  loading = true;
  quantity = 1;
  added = false;
  errorMsg = '';
  successMsg = '';
  newReview = { rating: 5, comment: '' };
  reviewSubmitted = false;

  constructor(
    private route: ActivatedRoute,
    private productService: ProductService,
    private cartService: CartService,
    public auth: AuthService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit() {
    const id = Number(this.route.snapshot.paramMap.get('id'));

    this.productService.getProduct(id).subscribe({
      next: (p: any) => {
        this.product = p;
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.loading = false;
        this.cdr.detectChanges();
      }
    });

    this.productService.getReviews(id).subscribe({
      next: (r: any[]) => {
        this.reviews = r;
        this.cdr.detectChanges();
      },
      error: () => {
        this.reviews = [];
      }
    });
  }

  decreaseQty() { if (this.quantity > 1) this.quantity--; }
  increaseQty() { if (this.quantity < this.product?.inventory?.quantity) this.quantity++; }

  addToCart() {
    this.errorMsg = '';
    this.successMsg = '';
    this.cartService.addToCart(this.product.id, this.quantity).subscribe({
      next: () => {
        this.added = true;
        this.successMsg = 'Produit ajouté au panier !';
        setTimeout(() => { this.added = false; this.successMsg = ''; }, 3000);
      },
      error: (err: any) => {
        this.errorMsg = err?.error?.message || 'Stock insuffisant pour cette quantité !';
        setTimeout(() => { this.errorMsg = ''; }, 3000);
      }
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
      this.cdr.detectChanges();
      setTimeout(() => this.reviewSubmitted = false, 2000);
    });
  }

  get stars() { return [1, 2, 3, 4, 5]; }

  get avgRating() {
    if (!this.reviews.length) return 0;
    return this.reviews.reduce((s: number, r: any) => s + r.rating, 0) / this.reviews.length;
  }
}