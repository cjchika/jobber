package com.cjchika.jobber.job.controller;

import com.cjchika.jobber.api.ApiResponse;
import com.cjchika.jobber.job.dto.JobRequestDTO;
import com.cjchika.jobber.user.dto.UserResponseDTO;
import com.cjchika.jobber.user.dto.LoginRequestDTO;
import com.cjchika.jobber.user.dto.LoginResponseDTO;
import com.cjchika.jobber.user.dto.UserRequestDTO;
import com.cjchika.jobber.user.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
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

    @PostMapping(value = "/register", produces = "application/json")
    @Operation(
            summary = "Create a new user",
            description = "This endpoint creates a new user by any of these roles - 'USER', 'EMPLOYER', 'ADMIN', If user role is Employer, include companyId assuming they're joining existing company or full company details i.e. name, description and website - this way, you're creating a user alongside a company, otherwise leave out company details for other roles.")
    public ResponseEntity<ApiResponse<UserResponseDTO>> register(@Valid @RequestBody UserRequestDTO userRequestDTO){

        UserResponseDTO newUserResponseDTO = authService.register(userRequestDTO);
        String location = baseUrl + newUserResponseDTO.getId();

        return ApiResponse.created(newUserResponseDTO, "User created successfully", location);
    }

    @PostMapping(value = "/login", produces = "application/json")
    @Operation(summary = "Log in a user", description = "Logs in an authenticated user")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO){
        LoginResponseDTO authenticatedUser = authService.login(loginRequestDTO);

        return ApiResponse.success(authenticatedUser, "Login Success", HttpStatus.OK);
    }
}
