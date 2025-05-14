package com.alten.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WishListItemDTO {
    private Long id;
    private ProductDTO product;
    
}
