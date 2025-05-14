package com.alten.shop.service;

import com.alten.shop.dto.ProductDTO;
import com.alten.shop.dto.ProductUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    ProductDTO createProduct(ProductDTO productDTO);
    Page<ProductDTO> getAllProducts(Pageable pageable);
    ProductDTO getProductById(Long id);

    ProductDTO updateProductPartial(Long id, ProductUpdateDTO updateDTO);
    
    void deleteProduct(Long id);
}
