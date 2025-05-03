package com.cjchika.jobber.category.controller;

import com.cjchika.jobber.api.ApiResponse;
import com.cjchika.jobber.category.dto.JobCategoryRequestDTO;
import com.cjchika.jobber.category.dto.JobCategoryResponseDTO;
import com.cjchika.jobber.category.service.JobCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/job-categories")
@Tag(name = "Job Categories", description = "Endpoints for managing job-category relationships")
public class JobCategoryController {
    private static final Logger logger = LoggerFactory.getLogger(JobCategoryController.class);
    private final JobCategoryService jobCategoryService;

    public JobCategoryController(JobCategoryService jobCategoryService) {
        this.jobCategoryService = jobCategoryService;
    }

    @PostMapping(produces = "application/json")
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYER', 'ROLE_ADMIN')")
    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Add category to job", description = "Associate a category with a job (Employer only)")
    public ResponseEntity<ApiResponse<JobCategoryResponseDTO>> addCategoryToJob(
            @Valid @RequestBody JobCategoryRequestDTO requestDTO) {
        try {
            logger.info("Adding category {} to job {}", requestDTO.getCategoryId(), requestDTO.getJobId());
            JobCategoryResponseDTO response = jobCategoryService.addCategoryToJob(requestDTO);
            return ApiResponse.success(response, "Category added to job successfully", HttpStatus.CREATED);
        } catch (Exception ex) {
            logger.error("Error adding category to job: {}", ex.getMessage());
            return ApiResponse.error(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/job/{jobId}", produces = "application/json")
    @Operation(summary = "Get categories for job", description = "Retrieve all categories associated with a job")
    public ResponseEntity<ApiResponse<List<JobCategoryResponseDTO>>> getCategoriesForJob(
            @PathVariable UUID jobId) {
        try {
            logger.info("Fetching categories for job {}", jobId);
            List<JobCategoryResponseDTO> categories = jobCategoryService.getCategoriesForJob(jobId);
            return ApiResponse.success(categories, "Job categories retrieved successfully", HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("Error fetching job categories: {}", ex.getMessage());
            return ApiResponse.error(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/category/{categoryId}", produces = "application/json")
    @Operation(summary = "Get jobs for category", description = "Retrieve all jobs associated with a category")
    public ResponseEntity<ApiResponse<List<JobCategoryResponseDTO>>> getJobsForCategory(
            @PathVariable UUID categoryId) {
        try {
            logger.info("Fetching jobs for category {}", categoryId);
            List<JobCategoryResponseDTO> jobs = jobCategoryService.getJobsForCategory(categoryId);
            return ApiResponse.success(jobs, "Category jobs retrieved successfully", HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("Error fetching category jobs: {}", ex.getMessage());
            return ApiResponse.error(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "/job/{jobId}/category/{categoryId}", produces = "application/json")
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYER', 'ROLE_ADMIN')")
    @SecurityRequirement(name = "Authorization")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remove category from job", description = "Disassociate a category from a job (Employer or Admin only)")
    public void removeCategoryFromJob(
            @PathVariable UUID jobId,
            @PathVariable UUID categoryId) {
        logger.info("Removing category {} from job {}", categoryId, jobId);
        jobCategoryService.removeCategoryFromJob(jobId, categoryId);
    }
}