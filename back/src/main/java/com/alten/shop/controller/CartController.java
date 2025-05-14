package com.alten.shop.controller;


import com.alten.shop.dto.CartDTO;
import com.alten.shop.service.CartService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("api/v1/cart")
@RequiredArgsConstructor
@Validated
@Tag(name = "Cart", description = "Cart management API")
@Slf4j
public class CartController {

    private final CartService cartService;
    
    @Operation(summary = "Get current user's cart", description = "Retrieves the cart for the authenticated user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cart retrieved successfully", 
                     content = @Content(schema = @Schema(implementation = CartDTO.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    @GetMapping
    public ResponseEntity<CartDTO> getCart(Authentication authentication) {
        String userEmail = authentication.getName();
        log.info("Start getCart - User email: {}", userEmail);
        CartDTO cartDTO = cartService.getCartDTO(userEmail);
        log.info("End getCart - Cart: {}", cartDTO);
        return ResponseEntity.ok(cartDTO);
    }
    
    @Operation(summary = "Add product to cart", description = "Adds specified quantity of a product to the user's cart")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product added to cart successfully", 
                     content = @Content(schema = @Schema(implementation = CartDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input or product not available", content = @Content),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    @PostMapping("/items")
    public ResponseEntity<?> addProductToCart(
            @Parameter(description = "Product ID to add to cart", required = true) 
            @RequestParam Long productId,
            @Parameter(description = "Quantity to add (minimum 1)", required = true) 
            @RequestParam @Min(value = 1, message = "Quantity must be at least 1") Integer quantity,
            Authentication authentication) {
        String userEmail = authentication.getName();
        log.info("Start addProductToCart - User email: {}, Product ID: {}, Quantity: {}", userEmail, productId, quantity);
        CartDTO cartDTO = cartService.addProductToCartAndReturnDTO(userEmail, productId, quantity);
        log.info("End addProductToCart - Updated cart: {}", cartDTO);
        return ResponseEntity.ok(cartDTO);
    }
    
    @Operation(summary = "Update cart item quantity", description = "Updates the quantity of a product in the user's cart")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cart item updated successfully", 
                     content = @Content(schema = @Schema(implementation = CartDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    @PutMapping("/items")
    public ResponseEntity<?> updateCartItem(
            @Parameter(description = "Product ID to update", required = true) 
            @RequestParam Long productId,
            @Parameter(description = "New quantity (minimum 1)", required = true) 
            @RequestParam @Min(value = 1, message = "Quantity must be at least 1") Integer quantity,
            Authentication authentication) {
        String userEmail = authentication.getName();
        log.info("Start updateCartItem - User email: {}, Product ID: {}, New quantity: {}", userEmail, productId, quantity);
        CartDTO cartDTO = cartService.updateCartItemAndReturnDTO(userEmail, productId, quantity);
        log.info("End updateCartItem - Updated cart: {}", cartDTO);
        return ResponseEntity.ok(cartDTO);
    }
    
    @Operation(summary = "Remove product from cart", description = "Removes a product from the user's cart")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product removed successfully", 
                     content = @Content(schema = @Schema(implementation = CartDTO.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    @DeleteMapping("/items")
    public ResponseEntity<?> removeProductFromCart(
            @Parameter(description = "Product ID to remove", required = true) 
            @RequestParam Long productId,
            Authentication authentication) {
        String userEmail = authentication.getName();
        log.info("Start removeProductFromCart - User email: {}, Product ID: {}", userEmail, productId);
        CartDTO cartDTO = cartService.removeProductFromCartAndReturnDTO(userEmail, productId);
        log.info("End removeProductFromCart - Updated cart: {}", cartDTO);
        return ResponseEntity.ok(cartDTO);
    }
    
    @Operation(summary = "Clear cart", description = "Removes all items from the user's cart")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cart cleared successfully", 
                     content = @Content(schema = @Schema(implementation = CartDTO.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    @DeleteMapping
    public ResponseEntity<?> clearCart(Authentication authentication) {
        String userEmail = authentication.getName();
        log.info("Start clearCart - User email: {}", userEmail);
        CartDTO cartDTO = cartService.clearCartAndReturnDTO(userEmail);
        log.info("End clearCart - Cleared cart: {}", cartDTO);
        return ResponseEntity.ok(cartDTO);
    }
}
