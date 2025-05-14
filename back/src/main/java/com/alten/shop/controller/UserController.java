package com.alten.shop.controller;

import com.alten.shop.dto.AuthResponse;
import com.alten.shop.dto.AuthRequest;
import com.alten.shop.dto.UserSignUpDTO;
import com.alten.shop.model.User;
import com.alten.shop.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
@Validated
@Tag(name = "Authentication", description = "User authentication and registration API")
@SecurityRequirements
@Slf4j
public class UserController {

    private final UserService userService;

    @Operation(summary = "Register a new user", description = "Creates a new user account in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User registered successfully", 
                     content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input or user already exists", 
                     content = @Content)
    })
    @PostMapping("/account")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserSignUpDTO signUpDTO) {
        log.info("Start registerUser - User data: {}", signUpDTO);
        userService.createUser(signUpDTO);
        log.info("End registerUser - User registered successfully");
        return ResponseEntity.ok("User registered successfully");
    }

    @Operation(summary = "User login", description = "Authenticates user and returns JWT token")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Authentication successful", 
                     content = @Content(schema = @Schema(implementation = AuthResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid credentials", 
                     content = @Content)
    })
    @PostMapping("/token")
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequest authRequest) {
        log.info("Start login - Email: {}", authRequest.getEmail());
        AuthResponse response = userService.login(authRequest);
        log.info("End login - Authentication successful");
        return ResponseEntity.ok(response);
    }

}
