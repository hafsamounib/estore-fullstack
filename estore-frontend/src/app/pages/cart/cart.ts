import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { CartService } from '../../services/cart';
import { OrderService } from '../../services/order';

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './cart.html',
  styleUrl: './cart.css'
})
export class CartComponent implements OnInit {
  cart: any = null;
  loading = true;
  ordering = false;
  orderSuccess = false;
  errorMsg = '';

  constructor(private cartService: CartService, private orderService: OrderService) {}

  ngOnInit() { this.loadCart(); }

loadCart() {
  console.log('=== LOAD CART CALLED ===');
  this.cartService.getCart().subscribe({
    next: (c: any) => { 
      console.log('=== CART DATA ===', c);
      this.cart = c; 
      this.loading = false; 
    },
    error: (err: any) => {
      console.log('=== CART ERROR ===', err);
      this.loading = false;
    }
  });
}
  updateQty(itemId: number, qty: number) {
    this.cartService.updateItem(itemId, qty).subscribe(c => this.cart = c);
  }

  remove(itemId: number) {
    this.cartService.removeItem(itemId).subscribe(c => this.cart = c);
  }

  placeOrder() {
    this.ordering = true;
    this.errorMsg = '';
    this.orderService.placeOrder().subscribe({
      next: () => {
        this.ordering = false;
        this.orderSuccess = true;
        this.loadCart();
      },
      error: (err) => {
        this.ordering = false;
        this.errorMsg = err.error?.message || 'Order failed. Please try again.';
      }
    });
  }

  get total() {
    return this.cart?.items?.reduce(
      (sum: number, item: any) => sum + item.product.price * item.quantity, 0) || 0;
  }
}