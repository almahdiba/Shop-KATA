package com.alten.shop.mapper;

import com.alten.shop.dto.CartDTO;
import com.alten.shop.dto.CartItemDTO;
import com.alten.shop.model.Cart;
import com.alten.shop.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;

@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface CartMapper {
    
    @Mapping(target = "items", source = "items")
    @Mapping(target = "total", expression = "java(calculateTotal(cart))")
    @Mapping(target = "itemCount", expression = "java(calculateItemCount(cart))")
    CartDTO toDTO(Cart cart);

    @Mapping(target = "items", ignore = true)
    Cart toEntity(CartDTO cartDTO);

    @Mapping(target = "product", source = "product")
    CartItemDTO toCartItemDTO(CartItem cartItem);

    @Mapping(target = "product", source = "product")
    CartItem toCartItem(CartItemDTO cartItemDTO);

    default BigDecimal calculateTotal(Cart cart) {
        if (cart == null || cart.getItems() == null) {
            return BigDecimal.ZERO;
        }
        return cart.getItems().stream()
                .map(item -> BigDecimal.valueOf(item.getProduct().getPrice())
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    default int calculateItemCount(Cart cart) {
        if (cart == null || cart.getItems() == null) {
            return 0;
        }
        return cart.getItems().stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }
}
