package com.cjchika.jobber.user.controller;

import com.cjchika.jobber.user.api.ApiResponse;
import com.cjchika.jobber.user.dto.UserRequestDTO;
import com.cjchika.jobber.user.dto.UserResponseDTO;
import com.cjchika.jobber.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User", description = "Endpoints for users")
public class UserController {

    @Value("${jobber.baseUrl}")
    private String baseUrl;
    private UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping(produces = "application/json")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all users", description = "This endpoint retrieves all users from the system")
    public ResponseEntity<ApiResponse<List<UserResponseDTO>>> getUsers(){
        List<UserResponseDTO> users = userService.getUsers();
        return ApiResponse.success(users, "Users retrieved successfully", HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    @PreAuthorize("hasRole('ADMIN') or #userId == principal.id")
    @Operation(summary = "Get a user", description = "This endpoint retrieves a user from the system")
    public ResponseEntity<ApiResponse<UserResponseDTO>> getUser(@PathVariable UUID userId){
        UserResponseDTO user = userService.getUser(userId);
        return ApiResponse.success(user, "User retrieved successfully", HttpStatus.OK);
    }

    @PatchMapping(value = "/{id}", produces = "application/json")
    @PreAuthorize("hasRole('ADMIN') or #userId == principal.id")
    @Operation(
            summary = "Update user",
            description = "This endpoint updates an existing user.")
    public ResponseEntity<ApiResponse<UserResponseDTO>> updateUser(@Valid @RequestBody UserRequestDTO userRequestDTO, @PathVariable UUID userId){

        UserResponseDTO newUserResponseDTO = userService.updateUser(userRequestDTO, userId);

        return ApiResponse.success(newUserResponseDTO, "User updated successfully", HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}", produces = "application/json")
    @PreAuthorize("hasRole('ADMIN') or #userId == principal.id")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Delete user",
            description = "This endpoint deletes a user.")
    public void deleteUser(@Valid @RequestParam UUID userId){
        userService.deleteUser(userId);
    }
}
