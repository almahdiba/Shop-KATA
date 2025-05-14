package com.alten.shop.controller;

import com.alten.shop.annotation.IsAdmin;
import com.alten.shop.dto.ProductDTO;
import com.alten.shop.dto.ProductUpdateDTO;
import com.alten.shop.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("api/v1/products")
@RequiredArgsConstructor
@Validated
@Tag(name = "Products", description = "Product management API")
@Slf4j
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "Create a new product", description = "Creates a new product in the catalog (Admin only)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Product created successfully",
                     content = @Content(schema = @Schema(implementation = ProductDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
        @ApiResponse(responseCode = "403", description = "Forbidden - admin access required", content = @Content)
    })
    @IsAdmin
    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(
            @Parameter(description = "Product data to create", required = true) 
            @Valid @RequestBody ProductDTO productDTO) {
        log.info("Start createProduct - Product data: {}", productDTO);
        ProductDTO createdProduct = productService.createProduct(productDTO);
        log.info("End createProduct - Created product: {}", createdProduct);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all products", description = "Retrieves a paginated list of all products in the catalog")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Products retrieved successfully",
                     content = @Content(schema = @Schema(implementation = Page.class)))
    })
    @GetMapping
    public ResponseEntity<Page<ProductDTO>> getAllProducts(
            @Parameter(description = "Pagination information (page, size, sort)", 
                       schema = @Schema(implementation = Pageable.class))
            Pageable pageable) {
        log.info("Start getAllProducts - Pageable: {}", pageable);
        Page<ProductDTO> products = productService.getAllProducts(pageable);
        log.info("End getAllProducts - Found {} products", products.getTotalElements());
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "Get product by ID", description = "Retrieves a specific product by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product found",
                     content = @Content(schema = @Schema(implementation = ProductDTO.class))),
        @ApiResponse(responseCode = "404", description = "Product not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(
            @Parameter(description = "Product ID", required = true)
            @PathVariable Long id) {
        log.info("Start getProductById - ID: {}", id);
        ProductDTO product = productService.getProductById(id);
        log.info("End getProductById - Found product: {}", product);
        return ResponseEntity.ok(product);
    }

    @Operation(summary = "Update product", description = "Updates an existing product with partial data (Admin only)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product updated successfully",
                     content = @Content(schema = @Schema(implementation = ProductDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
        @ApiResponse(responseCode = "403", description = "Forbidden - admin access required", content = @Content),
        @ApiResponse(responseCode = "404", description = "Product not found", content = @Content)
    })
    @IsAdmin
    @PatchMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(
            @Parameter(description = "Product ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "Partial product data for update", required = true)
            @Valid @RequestBody ProductUpdateDTO updateDTO) {
        log.info("Start updateProduct - ID: {}, Partial product data: {}", id, updateDTO);
        ProductDTO updatedProduct = productService.updateProductPartial(id, updateDTO);
        log.info("End updateProduct - Updated product: {}", updatedProduct);
        return ResponseEntity.ok(updatedProduct);
    }

    @Operation(summary = "Delete product", description = "Deletes a product from the catalog (Admin only)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Product deleted successfully", content = @Content),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
        @ApiResponse(responseCode = "403", description = "Forbidden - admin access required", content = @Content),
        @ApiResponse(responseCode = "404", description = "Product not found", content = @Content)
    })
    @IsAdmin
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @Parameter(description = "ID of the product to delete", required = true)
            @PathVariable Long id) {
        log.info("Start deleteProduct - ID: {}", id);
        productService.deleteProduct(id);
        log.info("End deleteProduct - Product deleted successfully");
        return ResponseEntity.noContent().build();
    }
}
