package com.alten.shop.service.impl;

import com.alten.shop.dto.ProductDTO;
import com.alten.shop.dto.ProductUpdateDTO;
import com.alten.shop.exception.ResourceNotFoundException;
import com.alten.shop.model.Product;
import com.alten.shop.repository.ProductRepository;
import com.alten.shop.service.ProductService;
import com.alten.shop.mapper.ProductMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public ProductDTO createProduct(ProductDTO productDTO) {
        log.info("Start createProduct - Product data: {}", productDTO);
        Product product = productMapper.toEntity(productDTO);
        Product savedProduct = productRepository.save(product);
        ProductDTO result = productMapper.toDTO(savedProduct);
        log.info("End createProduct - Created product: {}", result);
        return result;
    }

    @Override
    public Page<ProductDTO> getAllProducts(Pageable pageable) {
        log.info("Start getAllProducts - Pageable: {}", pageable);
        Page<ProductDTO> result = productRepository.findAll(pageable)
                .map(productMapper::toDTO);
        log.info("End getAllProducts - Found {} products", result.getTotalElements());
        return result;
    }

    @Override
    public ProductDTO getProductById(Long id) {
        log.info("Start getProductById - ID: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));
        ProductDTO result = productMapper.toDTO(product);
        log.info("End getProductById - Found product: {}", result);
        return result;
    }

    
    @Override
    @Transactional
    public void deleteProduct(Long id) {
        log.info("Start deleteProduct - ID: {}", id);
        if (!productRepository.existsById(id)) {
            log.error("Delete failed - Product not found with id: {}", id);
            throw new ResourceNotFoundException("Product not found with id " + id);
        }
        productRepository.deleteById(id);
        log.info("End deleteProduct - Product deleted successfully");
    }
    
    @Override
    @Transactional
    public ProductDTO updateProductPartial(Long id, ProductUpdateDTO updateDTO) {
        log.info("Start updateProductPartial - ID: {}, Product data: {}", id, updateDTO);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));
        
        productMapper.updateProductFromUpdateDTO(updateDTO, product);
        product.setId(id);
        Product updatedProduct = productRepository.save(product);
        
        ProductDTO result = productMapper.toDTO(updatedProduct);
        log.info("End updateProductPartial - Updated product: {}", result);
        return result;
    }
}
