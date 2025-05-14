package com.alten.shop.mapper;

import com.alten.shop.dto.UserSignUpDTO;
import com.alten.shop.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    
    @Mapping(target = "password", ignore = true)
    UserSignUpDTO toDTO(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cart", ignore = true)
    @Mapping(target = "wishList", ignore = true)
    User toEntity(UserSignUpDTO signUpDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "cart", ignore = true)
    @Mapping(target = "wishList", ignore = true)
    void updateUserFromDTO(UserSignUpDTO userDTO, @MappingTarget User user);
}
