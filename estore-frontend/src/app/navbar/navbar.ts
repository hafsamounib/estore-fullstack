import { Component, OnInit } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../services/auth';
import { CartService } from '../services/cart';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [RouterLink, RouterLinkActive, CommonModule],
  templateUrl: './navbar.html',
  styleUrl: './navbar.css'
})
export class NavbarComponent implements OnInit {
  cartCount = 0;
  user: any = null;

  constructor(public auth: AuthService, private cartService: CartService) {}

ngOnInit() {
  this.user = this.auth.getUser();
  if (this.auth.isLoggedIn()) {
    this.cartService.cartCount.subscribe((count: number) => this.cartCount = count);
    this.cartService.getCart().subscribe({
      next: () => {},
      error: () => {}
    });
  }
}

  logout() { this.auth.logout(); }
}