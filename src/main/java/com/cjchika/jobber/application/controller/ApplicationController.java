package com.cjchika.jobber.application.controller;

import com.cjchika.jobber.api.ApiResponse;
import com.cjchika.jobber.application.dto.ApplicationRequestDTO;
import com.cjchika.jobber.application.dto.ApplicationResponseDTO;
import com.cjchika.jobber.application.service.ApplicationService;
import com.cjchika.jobber.application.enums.Status;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@Tag(name = "Application", description = "Endpoints for job applications")
public class ApplicationController {

    @Value("${jobber.baseUrl}")
    private String baseUrl;
    private final ApplicationService applicationService;
    private static final Logger logger = LoggerFactory.getLogger(com.cjchika.jobber.job.controller.JobController.class);

    public ApplicationController(ApplicationService applicationService){
        this.applicationService = applicationService;
    }

    @PostMapping(value = "/applications", produces = "application/json")
    @PreAuthorize("hasRole('ROLE_USER')")
    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Submit application", description = "Submit job application (User only)")
    public ResponseEntity<ApiResponse<ApplicationResponseDTO>> postApplication(
            @RequestBody ApplicationRequestDTO applicationRequestDTO) {
        try {
            logger.info("Posting application status");
            ApplicationResponseDTO postApplication = applicationService.postApplication(applicationRequestDTO);
            return ApiResponse.success(postApplication, "Job applied successfully", HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("Error applying to this job application status: {}", ex.getMessage());
            return ApiResponse.error(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping(value = "/users/{userId}/applications",produces = "application/json")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Get user's applications", description = "Retrieve all applications for a specific user")
    public ResponseEntity<ApiResponse<List<ApplicationResponseDTO>>> getUserApplications(
           @PathVariable UUID userId
    ){
        try {
            logger.info("Fetching applications for user: {}", userId);
            List<ApplicationResponseDTO> applications = applicationService.getUserApplications(userId);
            return ApiResponse.success(applications, "Applications retrieved successfully", HttpStatus.OK);
        } catch (Exception ex){
            logger.error("Error fetching user applications: {}", ex.getMessage());
            return ApiResponse.error(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/jobs/{jobId}/applications",produces = "application/json")
    @PreAuthorize("hasRole('ROLE_EMPLOYER')")
    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Get job applications", description = "Retrieve all applications for a specific job (Employer only)")
    public ResponseEntity<ApiResponse<List<ApplicationResponseDTO>>> getJobApplications(@PathVariable UUID jobId){

        try {
            logger.info("Fetching applications for job: {}", jobId);
            List<ApplicationResponseDTO> applications = applicationService.getJobApplications(jobId);
            return ApiResponse.success(applications, "Applications retrieved successfully", HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("Error fetching job applications: {}", ex.getMessage());
            return ApiResponse.error(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping(value = "/applications/{id}", produces = "application/json")
    @PreAuthorize("hasRole('ROLE_EMPLOYER')")
    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Update application status", description = "Update the status of an application (Employer only)")
    public ResponseEntity<ApiResponse<ApplicationResponseDTO>> updateApplicationStatus(
            @PathVariable UUID id,
            @RequestParam Status status) {
        try {
            logger.info("Updating application status for: {}", id);
            ApplicationResponseDTO updatedApplication = applicationService.updateApplicationStatus(id, status);
            return ApiResponse.success(updatedApplication, "Application status updated successfully", HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("Error updating application status: {}", ex.getMessage());
            return ApiResponse.error(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
