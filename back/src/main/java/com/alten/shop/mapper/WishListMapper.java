package com.alten.shop.mapper;

import com.alten.shop.dto.WishListDTO;
import com.alten.shop.dto.WishListItemDTO;
import com.alten.shop.model.WishList;
import com.alten.shop.model.WishListItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface WishListMapper {
    
    @Mapping(target = "items", source = "items")
    @Mapping(target = "itemCount", expression = "java(calculateItemCount(wishList))")
    WishListDTO toDTO(WishList wishList);

    @Mapping(target = "items", ignore = true)
    WishList toEntity(WishListDTO wishListDTO);

    @Mapping(target = "product", source = "product")
    WishListItemDTO toWishListItemDTO(WishListItem wishListItem);

    @Mapping(target = "product", source = "product")
    WishListItem toWishListItem(WishListItemDTO wishListItemDTO);

    default int calculateItemCount(WishList wishList) {
        if (wishList == null || wishList.getItems() == null) {
            return 0;
        }
        return wishList.getItems().size();
    }
}
