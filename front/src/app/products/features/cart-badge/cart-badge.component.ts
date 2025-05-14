import { Component, computed, effect, inject } from "@angular/core";
import { Router } from "@angular/router";
import { BadgeModule } from "primeng/badge";
import { ButtonModule } from "primeng/button";
import { CartService } from "../../data-access/cart.service";
import { NgIf } from "@angular/common";

@Component({
  selector: "app-cart-badge",
  template: `
    <div class="relative">
      <button pButton 
        icon="pi pi-shopping-cart" 
        class="p-button-rounded p-button-text" 
        (click)="navigateToCart()"></button>
      <span class="p-badge p-badge-danger" 
        *ngIf="cartService.getCartCount() > 0"
        style="position: absolute; top: -8px; right: -8px;">
        {{ cartService.getCartCount() }}
      </span>
    </div>
  `,
  standalone: true,
  imports: [BadgeModule, ButtonModule, NgIf]
})
export class CartBadgeComponent {
  public readonly cartService = inject(CartService);
  private readonly router = inject(Router);

  constructor() {
    // Create an effect to track cart changes if needed in the future
    effect(() => {
      // This will re-run whenever cartItems signal changes
      const items = this.cartService.cartItems();
      // We could do something with items here if needed
    });
  }

  navigateToCart(): void {
    this.router.navigate(['/products/cart']);
  }
} 