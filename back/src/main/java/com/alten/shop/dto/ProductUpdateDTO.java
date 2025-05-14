package com.alten.shop.dto;

import com.alten.shop.enums.InventoryStatus;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductUpdateDTO {
    private String name;
    private String description;
    private String image;
    private String category;
    
    @Positive(message = "Price must be positive")
    private Double price;
    
    @PositiveOrZero(message = "Quantity must be positive or zero")
    private Integer quantity;
    
    private String internalReference;
    
    @Positive(message = "Shell ID must be positive")
    private Long shellId;
    
    private InventoryStatus inventoryStatus;
    
    @Min(value = 0, message = "Rating must be at least 0")
    @Max(value = 5, message = "Rating must be at most 5")
    private Double rating;
}
