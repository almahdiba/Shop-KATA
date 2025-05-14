package com.alten.shop.service;

import com.alten.shop.dto.AuthRequest;
import com.alten.shop.dto.AuthResponse;
import com.alten.shop.dto.UserSignUpDTO;
import com.alten.shop.model.User;

public interface UserService {
    
    AuthResponse login(AuthRequest loginDto);

    User createUser(UserSignUpDTO signUpDTO);

    User findByEmail(String email);

    User getCurrentUser(String email);
}
