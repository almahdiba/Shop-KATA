package com.alten.shop.dto;

import com.alten.shop.enums.InventoryStatus;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    private long id;

    @NotBlank(message = "Code cannot be blank")
    private String code;

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotBlank(message = "Description cannot be blank")
    private String description;

    private String image;

    @NotBlank(message = "Category cannot be blank")
    private String category;

    @Positive(message = "Price must be positive")
    private double price;

    @NotNull(message = "Quantity cannot be null")
    @PositiveOrZero(message = "Quantity must be positive or zero")
    private int quantity;

    private String internalReference;

    @Positive(message = "Shell ID must be positive")
    private long shellId;

    @NotNull(message = "Inventory status cannot be null")
    private InventoryStatus inventoryStatus;

    @Min(value = 0, message = "Rating must be at least 0")
    @Max(value = 5, message = "Rating must be at most 5")
    private double rating;

    private long createdAt;

    private long updatedAt;
}
