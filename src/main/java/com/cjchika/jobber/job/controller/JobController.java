package com.cjchika.jobber.job.controller;

import com.cjchika.jobber.api.ApiResponse;
import com.cjchika.jobber.job.dto.JobFilterRequest;
import com.cjchika.jobber.job.dto.JobRequestDTO;
import com.cjchika.jobber.job.dto.JobResponseDTO;
import com.cjchika.jobber.job.dto.JobUpdateDTO;
import com.cjchika.jobber.job.enums.JobType;
import com.cjchika.jobber.job.enums.Status;
import com.cjchika.jobber.job.service.JobService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@RequestMapping("/api/jobs")
@Tag(name = "Job", description = "Endpoints for jobs")
public class JobController {

    @Value("${jobber.baseUrl}")
    private String baseUrl;
    private JobService jobService;
    private static final Logger logger = LoggerFactory.getLogger(JobController.class);

    public JobController(JobService jobService){
        this.jobService = jobService;
    }

    @GetMapping(produces = "application/json")
    @Operation(summary = "Get all jobs", description = "This endpoint retrieves jobs with optional filters")
    public ResponseEntity<ApiResponse<List<JobResponseDTO>>> getFilteredJobs(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Double minSalary,
            @RequestParam(required = false) Double maxSalary,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) JobType jobType,
            @RequestParam(required = false) Status status
    ){
        try {
            logger.info("Fetching filtered jobs");

            JobFilterRequest filters = new JobFilterRequest();
            filters.setTitle(title);
            filters.setMinSalary(minSalary);
            filters.setMaxSalary(maxSalary);
            filters.setLocation(location);
            filters.setJobType(jobType);
            filters.setStatus(status);

            List<JobResponseDTO> jobs = jobService.getFilteredJobs(filters);

            return ApiResponse.success(jobs, "Filtered jobs retrieved successfully", HttpStatus.OK);
        } catch (Exception ex){
            logger.error("An error occurred while fetching jobs: {}", ex.getMessage());
            return ApiResponse.error( ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(produces = "application/json")
    @PreAuthorize("hasRole('ROLE_EMPLOYER')")
    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Create job", description = "This endpoint creates a job.")
    public ResponseEntity<ApiResponse<JobResponseDTO>> postJob(@Valid @RequestBody JobRequestDTO jobRequestDTO){

        try {
            logger.info("Creating job");
            JobResponseDTO newJobResponse = jobService.postJob(jobRequestDTO);

            return ApiResponse.success(newJobResponse, "Job created successfully", HttpStatus.CREATED);
        } catch (Exception ex){
            logger.error("An error occurred while creating jobs: {}", ex.getMessage());
            return ApiResponse.error( ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping(value = "/{jobId}", produces = "application/json")
    @Operation(summary = "Get a job", description = "This endpoint retrieves a job from the system")
    public ResponseEntity<ApiResponse<JobResponseDTO>> getJob(@PathVariable UUID jobId){
        try {
            logger.info("Getting job");
            JobResponseDTO job = jobService.getJob(jobId);
            return ApiResponse.success(job, "Job retrieved successfully", HttpStatus.OK);
        } catch (Exception ex){
            logger.error("An error occurred while getting job: {}", ex.getMessage());
            return ApiResponse.error( ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/{jobId}", produces = "application/json")
    @PreAuthorize("hasRole('ROLE_EMPLOYER')")
    @SecurityRequirement(name = "Authorization")
    @Operation(
            summary = "Update job",
            description = "This endpoint updates an existing job.")
    public ResponseEntity<ApiResponse<JobResponseDTO>> updateJob(@Valid @RequestBody JobUpdateDTO jobUpdateDTO, @PathVariable UUID jobId){

        try {
            logger.info("Updating a job");
            JobResponseDTO newJobResponseDTO = jobService.updateJob(jobUpdateDTO, jobId);

            return ApiResponse.success(newJobResponseDTO, "Job updated successfully", HttpStatus.OK);
        } catch (Exception ex){
            logger.error("An error occurred while updatomg job: {}", ex.getMessage());
            return ApiResponse.error( ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "/{jobId}", produces = "application/json")
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYER', 'ROLE_ADMIN')")
    @SecurityRequirement(name = "Authorization")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Delete job",
            description = "This endpoint deletes a job (only accessible by employer or admin).")
    public void deleteJob(@Valid @PathVariable UUID jobId){
        jobService.deleteJob(jobId);
    }
}
