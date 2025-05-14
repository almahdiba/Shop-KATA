package com.alten.shop.mapper;

import com.alten.shop.dto.ProductDTO;
import com.alten.shop.dto.ProductUpdateDTO;
import com.alten.shop.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductMapper {
    
    ProductDTO toDTO(Product product);
    
    Product toEntity(ProductDTO productDTO);
    
    void updateProductFromDTO(ProductDTO productDTO, @MappingTarget Product product);
    
    void updateProductFromUpdateDTO(ProductUpdateDTO updateDTO, @MappingTarget Product product);
}
