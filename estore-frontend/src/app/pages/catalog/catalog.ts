import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { ProductService } from '../../services/product';
import { CartService } from '../../services/cart';
import { AuthService } from '../../services/auth';
import { ChangeDetectorRef } from '@angular/core';

@Component({
  selector: 'app-catalog',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './catalog.html',
  styleUrl: './catalog.css'
})
export class CatalogComponent implements OnInit {

  products: any[] = [];
  categories: any[] = [];
  selectedCategory: number | null = null;
  searchQuery = '';
  loading = true;
  addedProductId: number | null = null;

  constructor(
    private productService: ProductService,
    private cartService: CartService,
    public auth: AuthService,
     private cdr: ChangeDetectorRef
  ) {}

  ngOnInit() {
    this.productService.getCategories().subscribe({
      next: (cats) => {
        console.log('CATEGORIES:', cats);
        this.categories = cats;
      },
      error: (err) => console.error('ERROR CATEGORIES:', err)
    });

    this.loadProducts();
  }

 loadProducts() {
  this.loading = true;

  this.productService.getProducts(
    this.searchQuery || undefined,
    this.selectedCategory
  ).subscribe({
    next: (data) => {
      this.products = data;
      this.loading = false;

      this.cdr.detectChanges();   
    },
    error: (err) => {
      console.error(err);
      this.loading = false;

      this.cdr.detectChanges();   
    }
  });
}

  filterByCategory(id: number | null) {
    console.log('CLICKED CATEGORY:', id);
    this.selectedCategory = id;
    this.searchQuery = '';
    this.loadProducts();
  }

  search() {
    this.selectedCategory = null;
    this.loadProducts();
  }

  addToCart(product: any) {
    if (!this.auth.isLoggedIn()) return;

    this.cartService.addToCart(product.id, 1).subscribe(() => {
      this.addedProductId = product.id;
      setTimeout(() => this.addedProductId = null, 1500);
    });
  }
}