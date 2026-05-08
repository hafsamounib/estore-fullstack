import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { OrderService } from '../../services/order';

@Component({
  selector: 'app-orders',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './orders.html',
  styleUrl: './orders.css'
})
export class OrdersComponent implements OnInit {
  orders: any[] = [];
  loading = true;
  expandedOrder: number | null = null;

  constructor(private orderService: OrderService) {}

  ngOnInit() {
    this.orderService.getMyOrders().subscribe({
      next: (o: any[]) => {
        this.orders = o;
        this.loading = false;
      },
      error: (error: any) => {
        console.error('Erreur orders:', error);
        this.loading = false;
      }
    });
  }

  toggle(id: number) {
    this.expandedOrder = this.expandedOrder === id ? null : id;
  }

  statusClass(status: string) {
    return {
      'PENDING': 'badge-warning',
      'CONFIRMED': 'badge-info',
      'SHIPPED': 'badge-primary',
      'DELIVERED': 'badge-success',
      'CANCELLED': 'badge-danger'
    }[status] || 'badge-warning';
  }

  formatDate(dateValue: any): string {
    if (!dateValue) return '';

    if (Array.isArray(dateValue)) {
      const [year, month, day, hour, minute] = dateValue;
      return `${day}/${month}/${year} ${hour}:${minute}`;
    }

    if (typeof dateValue === 'string' && dateValue.includes(',')) {
      const parts = dateValue.split(',').map(Number);
      const [year, month, day, hour, minute] = parts;
      return `${day}/${month}/${year} ${hour}:${minute}`;
    }

    return new Date(dateValue).toLocaleString();
  }
}
