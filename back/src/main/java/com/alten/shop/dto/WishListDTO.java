package com.alten.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WishListDTO {
    private Long id;
    private List<WishListItemDTO> items = new ArrayList<>();
    private int itemCount;
}
