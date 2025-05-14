import { Injectable, signal } from "@angular/core";
import { Product } from "./product.model";

export interface CartItem {
  product: Product;
  quantity: number;
}

@Injectable({
  providedIn: "root"
})
export class CartService {
  private readonly _cartItems = signal<CartItem[]>([]);
  public readonly cartItems = this._cartItems.asReadonly();

  public addToCart(product: Product): void {
    this._cartItems.update(items => {
      const existingItem = items.find(item => item.product.id === product.id);
      if (existingItem) {
        return items.map(item => 
          item.product.id === product.id 
            ? { ...item, quantity: item.quantity + 1 } 
            : item
        );
      } else {
        return [...items, { product, quantity: 1 }];
      }
    });
  }

  public removeFromCart(productId: number): void {
    this._cartItems.update(items => items.filter(item => item.product.id !== productId));
  }

  public decreaseQuantity(productId: number): void {
    this._cartItems.update(items => {
      const existingItem = items.find(item => item.product.id === productId);
      if (existingItem && existingItem.quantity > 1) {
        return items.map(item => 
          item.product.id === productId 
            ? { ...item, quantity: item.quantity - 1 } 
            : item
        );
      } else {
        return items.filter(item => item.product.id !== productId);
      }
    });
  }

  public getCartTotal(): number {
    return this._cartItems().reduce(
      (total, item) => total + (item.product.price * item.quantity), 
      0
    );
  }

  public getCartCount(): number {
    return this._cartItems().reduce(
      (count, item) => count + item.quantity, 
      0
    );
  }

  public clearCart(): void {
    this._cartItems.set([]);
  }
} 