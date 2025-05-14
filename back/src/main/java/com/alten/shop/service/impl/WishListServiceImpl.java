package com.alten.shop.service.impl;

import com.alten.shop.dto.ProductDTO;
import com.alten.shop.dto.WishListDTO;
import com.alten.shop.dto.WishListItemDTO;
import com.alten.shop.model.Product;
import com.alten.shop.model.User;
import com.alten.shop.model.WishList;
import com.alten.shop.model.WishListItem;
import com.alten.shop.repository.WishListRepository;
import com.alten.shop.service.ProductService;
import com.alten.shop.service.UserService;
import com.alten.shop.service.WishListService;
import com.alten.shop.mapper.WishListMapper;
import com.alten.shop.mapper.ProductMapper;
import lombok.extern.slf4j.Slf4j;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class WishListServiceImpl implements WishListService {

    private final WishListRepository wishListRepository;
    private final ProductService productService;
    private final UserService userService;
    private final WishListMapper wishListMapper;
    private final ProductMapper productMapper;

    @Override
    public WishList getWishList(String userEmail) {
        log.info("Start getWishList - User email: {}", userEmail);
        User user = userService.findByEmail(userEmail);
        WishList wishList = wishListRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("WishList not found for user"));
        log.info("End getWishList - Found wishlist: {}", wishList);
        return wishList;
    }

    @Override
    public WishListDTO getWishListDTO(String userEmail) {
        log.info("Start getWishListDTO - User email: {}", userEmail);
        WishList wishList = getWishList(userEmail);
        WishListDTO result = wishListMapper.toDTO(wishList);
        log.info("End getWishListDTO - Wishlist DTO: {}", result);
        return result;
    }

    @Override
    @Transactional
    public WishList addProductToWishList(String userEmail, Long productId) {
        log.info("Start addProductToWishList - User email: {}, Product ID: {}", userEmail, productId);
        User user = userService.findByEmail(userEmail);
        ProductDTO productDTO = productService.getProductById(productId);
        Product product = productMapper.toEntity(productDTO);

        WishList wishList = wishListRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("WishList not found for user"));

        boolean productExists = wishList.getItems().stream()
                .anyMatch(item -> item.getProduct().getId() == productId);

        if (!productExists) {
            WishListItem newItem = new WishListItem();
            newItem.setProduct(product);
            newItem.setWishList(wishList);
            wishList.getItems().add(newItem);
            log.info("Added new item to wishlist");
        } else {
            log.info("Product already in wishlist, no action taken");
        }

        WishList result = wishListRepository.save(wishList);
        log.info("End addProductToWishList - Updated wishlist: {}", result);
        return result;
    }

    @Override
    public WishListDTO addProductToWishListAndReturnDTO(String userEmail, Long productId) {
        log.info("Start addProductToWishListAndReturnDTO - User email: {}, Product ID: {}", userEmail, productId);
        WishList wishList = addProductToWishList(userEmail, productId);
        WishListDTO result = wishListMapper.toDTO(wishList);
        log.info("End addProductToWishListAndReturnDTO - Wishlist DTO: {}", result);
        return result;
    }

    @Override
    @Transactional
    public WishList removeProductFromWishList(String userEmail, Long productId) {
        log.info("Start removeProductFromWishList - User email: {}, Product ID: {}", userEmail, productId);
        User user = userService.findByEmail(userEmail);

        WishList wishList = wishListRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("WishList not found for user"));

        boolean removed = wishList.getItems().removeIf(item -> item.getProduct().getId() == productId);
        if (removed) {
            log.info("Product removed from wishlist");
        } else {
            log.warn("Product not found in wishlist: {}", productId);
        }

        WishList result = wishListRepository.save(wishList);
        log.info("End removeProductFromWishList - Updated wishlist: {}", result);
        return result;
    }

    @Override
    public WishListDTO removeProductFromWishListAndReturnDTO(String userEmail, Long productId) {
        log.info("Start removeProductFromWishListAndReturnDTO - User email: {}, Product ID: {}", userEmail, productId);
        WishList wishList = removeProductFromWishList(userEmail, productId);
        WishListDTO result = wishListMapper.toDTO(wishList);
        log.info("End removeProductFromWishListAndReturnDTO - Wishlist DTO: {}", result);
        return result;
    }
    
    @Override
    @Transactional
    public WishList clearWishList(String userEmail) {
        log.info("Start clearWishList - User email: {}", userEmail);
        User user = userService.findByEmail(userEmail);

        WishList wishList = wishListRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("WishList not found for user"));

        int itemCount = wishList.getItems().size();
        wishList.getItems().clear();
        log.info("Cleared {} items from wishlist", itemCount);

        WishList result = wishListRepository.save(wishList);
        log.info("End clearWishList - Cleared wishlist: {}", result);
        return result;
    }
    
    @Override
    public WishListDTO clearWishListAndReturnDTO(String userEmail) {
        log.info("Start clearWishListAndReturnDTO - User email: {}", userEmail);
        WishList wishList = clearWishList(userEmail);
        WishListDTO result = wishListMapper.toDTO(wishList);
        log.info("End clearWishListAndReturnDTO - Wishlist DTO: {}", result);
        return result;
    }
}
