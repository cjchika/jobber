package com.cjchika.jobber.user.controller;

import com.cjchika.jobber.user.api.ApiResponse;
import com.cjchika.jobber.user.dto.UserRequestDTO;
import com.cjchika.jobber.user.dto.UserResponseDTO;
import com.cjchika.jobber.user.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Endpoints for authentication")
public class AuthController {

    @Value("${jobber.baseUrl}")
    private String baseUrl;

    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping(produces = "application/json")
    @Operation(summary = "Create a new user", description = "This endpoint creates a new user by any of these roles - 'JOB_SEEKER', 'EMPLOYER', 'ADMIN' ")
    public ResponseEntity<ApiResponse<UserResponseDTO>> register(@Valid @RequestBody UserRequestDTO userRequestDTO){

        UserResponseDTO newUserResponseDTO = authService.register(userRequestDTO);
        String location = baseUrl + newUserResponseDTO.getId();

        return ApiResponse.created(newUserResponseDTO, "User created successfully", location);
    }
}
