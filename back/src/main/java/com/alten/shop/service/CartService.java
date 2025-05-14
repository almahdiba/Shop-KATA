package com.alten.shop.service;

import com.alten.shop.dto.CartDTO;
import com.alten.shop.model.Cart;

public interface CartService {

    Cart getCart(String userEmail);

    CartDTO getCartDTO(String userEmail);

    Cart addProductToCart(String userEmail, Long productId, Integer quantity);

    CartDTO addProductToCartAndReturnDTO(String userEmail, Long productId, Integer quantity);

    Cart updateCartItem(String userEmail, Long productId, Integer quantity);

    CartDTO updateCartItemAndReturnDTO(String userEmail, Long productId, Integer quantity);

    Cart removeProductFromCart(String userEmail, Long productId);

    CartDTO removeProductFromCartAndReturnDTO(String userEmail, Long productId);

    Cart clearCart(String userEmail);

    CartDTO clearCartAndReturnDTO(String userEmail);
}
