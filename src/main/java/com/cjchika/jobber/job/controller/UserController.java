package com.cjchika.jobber.job.controller;

import com.cjchika.jobber.api.ApiResponse;
import com.cjchika.jobber.job.dto.JobResponseDTO;
import com.cjchika.jobber.job.service.JobService;
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
    private JobService userService;

    public UserController(JobService userService){
        this.userService = userService;
    }

    @GetMapping(produces = "application/json")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Get all users", description = "This endpoint retrieves all users from the system")
    public ResponseEntity<ApiResponse<List<JobResponseDTO>>> getUsers(){
        List<JobResponseDTO> users = userService.getUsers();
        return ApiResponse.success(users, "Users retrieved successfully", HttpStatus.OK);
    }

    @GetMapping(value = "/{userId}", produces = "application/json")
//    @PreAuthorize("hasRole('ROLE_ADMIN') or #userId == principal.id")
    @Operation(summary = "Get a user", description = "This endpoint retrieves a user from the system")
    public ResponseEntity<ApiResponse<JobResponseDTO>> getUser(@PathVariable UUID userId){
        JobResponseDTO user = userService.getUser(userId);
        return ApiResponse.success(user, "User retrieved successfully", HttpStatus.OK);
    }

    @PutMapping(value = "/{userId}", produces = "application/json")
    @PreAuthorize("hasRole('ROLE_ADMIN') or #userId == principal.id")
    @Operation(
            summary = "Update user",
            description = "This endpoint updates an existing user.")
    public ResponseEntity<ApiResponse<JobResponseDTO>> updateUser(@Valid @RequestBody UserUpdateDTO userUpdateDTO, @PathVariable UUID userId){

        JobResponseDTO newUserResponseDTO = userService.updateUser(userUpdateDTO, userId);

        return ApiResponse.success(newUserResponseDTO, "User updated successfully", HttpStatus.OK);
    }

    @DeleteMapping(value = "/{userId}", produces = "application/json")
    @PreAuthorize("hasRole('ROLE_ADMIN') or #userId == principal.id")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Delete user",
            description = "This endpoint deletes a user.")
    public void deleteUser(@Valid @RequestParam UUID userId){
        userService.deleteUser(userId);
    }
}
