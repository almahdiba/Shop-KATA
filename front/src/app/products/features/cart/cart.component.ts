import { Component, OnInit, inject, signal } from "@angular/core";
import { CurrencyPipe, NgClass, NgFor, NgIf } from "@angular/common";
import { CartService, CartItem } from "../../data-access/cart.service";
import { ButtonModule } from "primeng/button";
import { CardModule } from "primeng/card";
import { TableModule } from "primeng/table";
import { Router } from "@angular/router";

@Component({
  selector: "app-cart",
  templateUrl: "./cart.component.html",
  styleUrls: ["./cart.component.scss"],
  standalone: true,
  imports: [
    NgFor, 
    NgIf, 
    NgClass, 
    CurrencyPipe, 
    ButtonModule, 
    CardModule, 
    TableModule
  ]
})
export class CartComponent {
  public readonly cartService = inject(CartService);
  private readonly router = inject(Router);

  removeItem(productId: number): void {
    this.cartService.removeFromCart(productId);
  }

  decreaseQuantity(productId: number): void {
    this.cartService.decreaseQuantity(productId);
  }

  increaseQuantity(productId: number): void {
    const item = this.cartService.cartItems().find(item => item.product.id === productId);
    if (item) {
      this.cartService.addToCart(item.product);
    }
  }

  goToProductList(): void {
    this.router.navigate(['/products']);
  }

  clearCart(): void {
    this.cartService.clearCart();
  }
} 