package com.alten.shop.controller;

import com.alten.shop.dto.WishListDTO;
import com.alten.shop.service.WishListService;
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
@RequestMapping("api/v1/wishList")
@Validated
@RequiredArgsConstructor
@Tag(name = "Wishlist", description = "Wishlist management API")
@Slf4j
public class WishListController {

    private final WishListService wishListService;
    
    @Operation(summary = "Get user's wishlist", description = "Retrieves the wishlist for the authenticated user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Wishlist retrieved successfully", 
                     content = @Content(schema = @Schema(implementation = WishListDTO.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    @GetMapping
    public ResponseEntity<WishListDTO> getWishList(Authentication authentication) {
        String userEmail = authentication.getName();
        log.info("Start getWishList - User email: {}", userEmail);
        WishListDTO wishListDTO = wishListService.getWishListDTO(userEmail);
        log.info("End getWishList - Wishlist: {}", wishListDTO);
        return ResponseEntity.ok(wishListDTO);
    }
    
    @Operation(summary = "Add product to wishlist", description = "Adds a product to the user's wishlist")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product added to wishlist successfully",
                     content = @Content(schema = @Schema(implementation = WishListDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input or product not available", content = @Content),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    @PostMapping("/items")
    public ResponseEntity<?> addProductToWishList(
            @Parameter(description = "Product ID to add to wishlist", required = true)
            @RequestParam Long productId,
            Authentication authentication) {
        String userEmail = authentication.getName();
        log.info("Start addProductToWishList - User email: {}, Product ID: {}", userEmail, productId);
        WishListDTO wishListDTO = wishListService.addProductToWishListAndReturnDTO(userEmail, productId);
        log.info("End addProductToWishList - Updated wishlist: {}", wishListDTO);
        return ResponseEntity.ok(wishListDTO);
    }
    
    @Operation(summary = "Remove product from wishlist", description = "Removes a product from the user's wishlist")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product removed from wishlist successfully",
                     content = @Content(schema = @Schema(implementation = WishListDTO.class))),
        @ApiResponse(responseCode = "400", description = "Product not in wishlist", content = @Content),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    @DeleteMapping("/items/{productId}")
    public ResponseEntity<?> removeProductFromWishList(
            @Parameter(description = "Product ID to remove from wishlist", required = true)
            @PathVariable Long productId,
            Authentication authentication) {
        String userEmail = authentication.getName();
        log.info("Start removeProductFromWishList - User email: {}, Product ID: {}", userEmail, productId);
        WishListDTO wishListDTO = wishListService.removeProductFromWishListAndReturnDTO(userEmail, productId);
        log.info("End removeProductFromWishList - Updated wishlist: {}", wishListDTO);
        return ResponseEntity.ok(wishListDTO);
    }
    
    @Operation(summary = "Clear wishlist", description = "Removes all items from the user's wishlist")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Wishlist cleared successfully", 
                     content = @Content(schema = @Schema(implementation = WishListDTO.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    @DeleteMapping
    public ResponseEntity<?> clearWishList(Authentication authentication) {
        String userEmail = authentication.getName();
        log.info("Start clearWishList - User email: {}", userEmail);
        WishListDTO wishListDTO = wishListService.clearWishListAndReturnDTO(userEmail);
        log.info("End clearWishList - Cleared wishlist: {}", wishListDTO);
        return ResponseEntity.ok(wishListDTO);
    }
}
