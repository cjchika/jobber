package com.cjchika.jobber.user.controller;

import com.cjchika.jobber.api.ApiResponse;
import com.cjchika.jobber.exception.JobberException;
import com.cjchika.jobber.user.dto.UserResponseDTO;
import com.cjchika.jobber.user.dto.UserUpdateDTO;
import com.cjchika.jobber.user.repository.UserRepository;
import com.cjchika.jobber.user.service.ResumeStorageService;
import com.cjchika.jobber.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User", description = "Endpoints for users")
public class UserController {

    @Value("${jobber.baseUrl}")
    private String baseUrl;
    private final UserService userService;
    private final ResumeStorageService resumeStorageService;

    private static final Logger logger = LoggerFactory.getLogger(com.cjchika.jobber.job.controller.JobController.class);


    public UserController(UserService userService,
                          ResumeStorageService resumeStorageService){
        this.userService = userService;
        this.resumeStorageService = resumeStorageService;
    }

    @GetMapping(produces = "application/json")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Get all users", description = "This endpoint retrieves all users from the system")
    public ResponseEntity<ApiResponse<List<UserResponseDTO>>> getUsers(){
        List<UserResponseDTO> users = userService.getUsers();
        return ApiResponse.success(users, "Users retrieved successfully", HttpStatus.OK);
    }

    @GetMapping(value = "/{userId}", produces = "application/json")
//    @PreAuthorize("hasRole('ROLE_ADMIN') or #userId == principal.id")
    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Get a user", description = "This endpoint retrieves a user from the system")
    public ResponseEntity<ApiResponse<UserResponseDTO>> getUser(@PathVariable UUID userId){
        UserResponseDTO user = userService.getUser(userId);
        return ApiResponse.success(user, "User retrieved successfully", HttpStatus.OK);
    }

    @PutMapping(value = "/{userId}", produces = "application/json")
    @PreAuthorize("hasRole('ROLE_ADMIN') or #userId == principal.id")
    @SecurityRequirement(name = "Authorization")
    @Operation(
            summary = "Update user",
            description = "This endpoint updates an existing user.")
    public ResponseEntity<ApiResponse<UserResponseDTO>> updateUser(@Valid @RequestBody UserUpdateDTO userUpdateDTO, @PathVariable UUID userId){

        UserResponseDTO newUserResponseDTO = userService.updateUser(userUpdateDTO, userId);

        return ApiResponse.success(newUserResponseDTO, "User updated successfully", HttpStatus.OK);
    }

    @DeleteMapping(value = "/{userId}", produces = "application/json")
    @PreAuthorize("hasRole('ROLE_ADMIN') or #userId == principal.id")
    @SecurityRequirement(name = "Authorization")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Delete user",
            description = "This endpoint deletes a user.")
    public void deleteUser(@Valid @PathVariable UUID userId){
        userService.deleteUser(userId);
    }

    @PostMapping(value = "/{userId}/resume", produces = "application/json", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ROLE_USER') or #userId == principal.id")
    @SecurityRequirement(name = "Authorization")
    @Operation(
            summary = "Upload resume",
            description = "This endpoint uploads a resume.")
    public ResponseEntity<ApiResponse<String>> uploadResume(
            @PathVariable UUID userId,
            @RequestParam("file") MultipartFile file) throws IOException {

        try {
            logger.info("Uploading resume");

            UserResponseDTO user = userService.getUser(userId);

            // Delete old resume if exists
            if (user.getResumeUrl() != null) {
                try {
                    resumeStorageService.deleteResume(user.getResumeUrl());
                } catch (Exception ex) {
                    logger.warn("Failed to delete old resume, proceeding with upload", ex);
                }
            }


            UserUpdateDTO userUpdateDTO = new UserUpdateDTO();

            String resumeUrl = resumeStorageService.uploadResume(file, userId);
            userUpdateDTO.setResumeUrl(resumeUrl);
            userService.updateUser(userUpdateDTO, userId);

            return ApiResponse.success(resumeUrl, "Resume uploaded successfully", HttpStatus.CREATED);
        } catch (Exception ex){
            logger.error("Error uploading resume: {}", ex.getMessage());
            return ApiResponse.error(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "/{userId}/resume", produces = "application/json")
    @PreAuthorize("hasRole('ROLE_USER') or #userId == principal.id")
    @SecurityRequirement(name = "Authorization")
    @Operation(
            summary = "Delete resume",
            description = "This endpoint deletes a user's resume.")
    public ResponseEntity<ApiResponse<Object>> deleteResume(@Valid @PathVariable UUID userId){

        UserResponseDTO user = userService.getUser(userId);

        if(user.getResumeUrl() == null){
            throw new JobberException("No resume exists for this user", HttpStatus.NOT_FOUND);
        }

        UserUpdateDTO userUpdateDTO = new UserUpdateDTO();

        resumeStorageService.deleteResume(user.getResumeUrl());
        userUpdateDTO.setResumeUrl(null);
        userService.updateUser(userUpdateDTO, userId);

        return ApiResponse.success(null, "Resume deleted successfully", HttpStatus.OK);
    }



















}
