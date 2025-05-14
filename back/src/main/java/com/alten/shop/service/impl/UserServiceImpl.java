package com.alten.shop.service.impl;

import com.alten.shop.config.AppProperties;
import com.alten.shop.config.security.JwtTokenUtil;
import com.alten.shop.dto.AuthRequest;
import com.alten.shop.dto.AuthResponse;
import com.alten.shop.dto.UserSignUpDTO;
import com.alten.shop.exception.FunctionalException;
import com.alten.shop.model.Cart;
import com.alten.shop.model.User;
import com.alten.shop.model.WishList;
import com.alten.shop.repository.UserRepository;
import com.alten.shop.service.UserService;
import com.alten.shop.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserMapper userMapper;
    private final AppProperties appProperties;
        
    @Override
    public AuthResponse login(AuthRequest authRequest) {
        log.info("Start login - Email: {}", authRequest.getEmail());
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
            );
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            User user = userRepository.findByEmailIgnoreCase(authRequest.getEmail())
                    .orElseThrow(() -> new FunctionalException("User not found"));
            
            boolean isAdmin = appProperties.getAdminEmail().equalsIgnoreCase(user.getEmail());
            String token = jwtTokenUtil.generateToken(user.getEmail(), isAdmin);
            
            log.info("End login - Authentication successful for user: {}", user.getEmail());
            return new AuthResponse(token);
        } catch (BadCredentialsException e) {
            log.error("Login failed - Invalid credentials for email: {}", authRequest.getEmail());
            throw new FunctionalException("Invalid email or password");
        }
    }

    @Override
    public User createUser(UserSignUpDTO signUpDTO) {
        log.info("Start createUser - User data: {}", signUpDTO);
        if (userRepository.existsByEmail(signUpDTO.getEmail())) {
            log.error("User creation failed - Email already exists: {}", signUpDTO.getEmail());
            throw new FunctionalException("Email is already in use");
        }

        if (userRepository.existsByUsername(signUpDTO.getUsername())) {
            log.error("User creation failed - Username already exists: {}", signUpDTO.getUsername());
            throw new FunctionalException("Username is already in use");
        }

        User user = userMapper.toEntity(signUpDTO);
        user.setPassword(passwordEncoder.encode(signUpDTO.getPassword()));

        Cart cart = new Cart();
        cart.setUser(user);
        user.setCart(cart);

        WishList wishList = new WishList();
        wishList.setUser(user);
        user.setWishList(wishList);

        User savedUser = userRepository.save(user);
        log.info("End createUser - Created user: {}", savedUser);
        return savedUser;
    }

    @Override
    public User findByEmail(String email) {
        log.info("Start findByEmail - Email: {}", email);
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new FunctionalException("User not found"));
        log.info("End findByEmail - Found user: {}", user);
        return user;
    }

    @Override
    public User getCurrentUser(String email) {
        log.info("Start getCurrentUser - Email: {}", email);
        User user = findByEmail(email);
        log.info("End getCurrentUser - Found user: {}", user);
        return user;
    }
}
