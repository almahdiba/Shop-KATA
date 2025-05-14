package com.alten.shop.service;

import com.alten.shop.dto.ProductDTO;
import com.alten.shop.dto.WishListDTO;
import com.alten.shop.model.Product;
import com.alten.shop.model.WishList;

public interface WishListService {

    WishList getWishList(String userEmail);

    WishListDTO getWishListDTO(String userEmail);

    WishList addProductToWishList(String userEmail, Long productId);

    WishListDTO addProductToWishListAndReturnDTO(String userEmail, Long productId);

    WishList removeProductFromWishList(String userEmail, Long productId);

    WishListDTO removeProductFromWishListAndReturnDTO(String userEmail, Long productId);

    WishList clearWishList(String userEmail);

    WishListDTO clearWishListAndReturnDTO(String userEmail);
}
