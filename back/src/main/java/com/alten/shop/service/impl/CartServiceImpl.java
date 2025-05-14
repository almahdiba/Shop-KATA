package com.alten.shop.service.impl;

import com.alten.shop.dto.CartDTO;
import com.alten.shop.dto.CartItemDTO;
import com.alten.shop.dto.ProductDTO;
import com.alten.shop.model.Cart;
import com.alten.shop.model.CartItem;
import com.alten.shop.model.Product;
import com.alten.shop.model.User;
import com.alten.shop.repository.CartRepository;
import com.alten.shop.service.ProductService;
import com.alten.shop.service.UserService;
import com.alten.shop.service.CartService;
import com.alten.shop.mapper.CartMapper;
import com.alten.shop.mapper.ProductMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductService productService;
    private final UserService userService;
    private final CartMapper cartMapper;
    private final ProductMapper productMapper;

    @Override
    public Cart getCart(String userEmail) {
        log.info("Start getCart - User email: {}", userEmail);
        User user = userService.findByEmail(userEmail);
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found for user"));
        log.info("End getCart - Found cart: {}", cart);
        return cart;
    }

    @Override
    public CartDTO getCartDTO(String userEmail) {
        log.info("Start getCartDTO - User email: {}", userEmail);
        Cart cart = getCart(userEmail);
        CartDTO result = cartMapper.toDTO(cart);
        log.info("End getCartDTO - Cart DTO: {}", result);
        return result;
    }

    @Override
    @Transactional
    public Cart addProductToCart(String userEmail, Long productId, Integer quantity) {
        log.info("Start addProductToCart - User email: {}, Product ID: {}, Quantity: {}", userEmail, productId, quantity);
        User user = userService.findByEmail(userEmail);
        ProductDTO productDTO = productService.getProductById(productId);
        Product product = productMapper.toEntity(productDTO);

        if (productDTO.getQuantity() < quantity) {
            log.error("Not enough stock available for product ID: {}", productId);
            throw new RuntimeException("Not enough stock available");
        }

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found for user"));

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId() == productId)
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            log.info("Updated existing cart item quantity to: {}", item.getQuantity());
        } else {
            CartItem newItem = new CartItem();
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            newItem.setCart(cart);
            cart.getItems().add(newItem);
            log.info("Added new item to cart");
        }

        Cart result = cartRepository.save(cart);
        log.info("End addProductToCart - Updated cart: {}", result);
        return result;
    }

    @Override
    public CartDTO addProductToCartAndReturnDTO(String userEmail, Long productId, Integer quantity) {
        log.info("Start addProductToCartAndReturnDTO - User email: {}, Product ID: {}, Quantity: {}", userEmail, productId, quantity);
        Cart cart = addProductToCart(userEmail, productId, quantity);
        CartDTO result = cartMapper.toDTO(cart);
        log.info("End addProductToCartAndReturnDTO - Cart DTO: {}", result);
        return result;
    }

    @Override
    @Transactional
    public Cart updateCartItem(String userEmail, Long productId, Integer quantity) {
        log.info("Start updateCartItem - User email: {}, Product ID: {}, New quantity: {}", userEmail, productId, quantity);
        User user = userService.findByEmail(userEmail);
        ProductDTO productDTO = productService.getProductById(productId);

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found for user"));

        CartItem item = cart.getItems().stream()
                .filter(i -> i.getProduct().getId() == Long.valueOf(productId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Product not found in cart"));

        if (quantity <= 0) {
            cart.getItems().remove(item);
            log.info("Removed item from cart as quantity was {}", quantity);
        } else {
            if (productDTO.getQuantity() < quantity) {
                log.error("Not enough stock available for product ID: {}", productId);
                throw new RuntimeException("Not enough stock available");
            }
            item.setQuantity(quantity);
            log.info("Updated cart item quantity to: {}", quantity);
        }

        Cart result = cartRepository.save(cart);
        log.info("End updateCartItem - Updated cart: {}", result);
        return result;
    }

    @Override
    public CartDTO updateCartItemAndReturnDTO(String userEmail, Long productId, Integer quantity) {
        log.info("Start updateCartItemAndReturnDTO - User email: {}, Product ID: {}, New quantity: {}", userEmail, productId, quantity);
        Cart cart = updateCartItem(userEmail, productId, quantity);
        CartDTO result = cartMapper.toDTO(cart);
        log.info("End updateCartItemAndReturnDTO - Cart DTO: {}", result);
        return result;
    }

    @Override
    @Transactional
    public Cart removeProductFromCart(String userEmail, Long productId) {
        log.info("Start removeProductFromCart - User email: {}, Product ID: {}", userEmail, productId);
        User user = userService.findByEmail(userEmail);

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found for user"));

        boolean removed = cart.getItems().removeIf(item -> item.getProduct().getId() == productId);
        if (removed) {
            log.info("Product removed from cart");
        } else {
            log.warn("Product not found in cart: {}", productId);
        }

        Cart result = cartRepository.save(cart);
        log.info("End removeProductFromCart - Updated cart: {}", result);
        return result;
    }

    @Override
    public CartDTO removeProductFromCartAndReturnDTO(String userEmail, Long productId) {
        log.info("Start removeProductFromCartAndReturnDTO - User email: {}, Product ID: {}", userEmail, productId);
        Cart cart = removeProductFromCart(userEmail, productId);
        CartDTO result = cartMapper.toDTO(cart);
        log.info("End removeProductFromCartAndReturnDTO - Cart DTO: {}", result);
        return result;
    }

    @Override
    @Transactional
    public Cart clearCart(String userEmail) {
        log.info("Start clearCart - User email: {}", userEmail);
        User user = userService.findByEmail(userEmail);

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found for user"));

        int itemCount = cart.getItems().size();
        cart.getItems().clear();
        log.info("Cleared {} items from cart", itemCount);

        Cart result = cartRepository.save(cart);
        log.info("End clearCart - Cleared cart: {}", result);
        return result;
    }

    @Override
    public CartDTO clearCartAndReturnDTO(String userEmail) {
        log.info("Start clearCartAndReturnDTO - User email: {}", userEmail);
        Cart cart = clearCart(userEmail);
        CartDTO result = cartMapper.toDTO(cart);
        log.info("End clearCartAndReturnDTO - Cart DTO: {}", result);
        return result;
    }
}
