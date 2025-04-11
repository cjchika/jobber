package com.cjchika.jobber.job.controller;

import com.cjchika.jobber.api.ApiResponse;
import com.cjchika.jobber.job.dto.JobRequestDTO;
import com.cjchika.jobber.job.dto.JobResponseDTO;
import com.cjchika.jobber.job.dto.JobUpdateDTO;
import com.cjchika.jobber.job.service.JobService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
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

    public JobController(JobService jobService){
        this.jobService = jobService;
    }

    @GetMapping(produces = "application/json")
    @Operation(summary = "Get all jobs", description = "This endpoint retrieves all jobs from the system")
    public ResponseEntity<ApiResponse<List<JobResponseDTO>>> getJobs(){
        List<JobResponseDTO> jobs = jobService.getJobs();
        return ApiResponse.success(jobs, "Jobs retrieved successfully", HttpStatus.OK);
    }

    @PostMapping(produces = "application/json")
    @PreAuthorize("hasRole('ROLE_EMPLOYER')")
    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Create job", description = "This endpoint creates a job.")
    public ResponseEntity<ApiResponse<JobResponseDTO>> postJob(@Valid @RequestBody JobRequestDTO jobRequestDTO){

        JobResponseDTO newJobResponse = jobService.postJob(jobRequestDTO);

        return ApiResponse.success(newJobResponse, "Job created successfully", HttpStatus.CREATED);
    }

    @GetMapping(value = "/{jobId}", produces = "application/json")
    @Operation(summary = "Get a job", description = "This endpoint retrieves a job from the system")
    public ResponseEntity<ApiResponse<JobResponseDTO>> getJob(@PathVariable UUID jobId){
        JobResponseDTO job = jobService.getJob(jobId);
        return ApiResponse.success(job, "Job retrieved successfully", HttpStatus.OK);
    }

    @PutMapping(value = "/{jobId}", produces = "application/json")
    @PreAuthorize("hasRole('ROLE_EMPLOYER')")
    @SecurityRequirement(name = "Authorization")
    @Operation(
            summary = "Update job",
            description = "This endpoint updates an existing job.")
    public ResponseEntity<ApiResponse<JobResponseDTO>> updateJob(@Valid @RequestBody JobUpdateDTO jobUpdateDTO, @PathVariable UUID jobId){

        JobResponseDTO newJobResponseDTO = jobService.updateJob(jobUpdateDTO, jobId);

        return ApiResponse.success(newJobResponseDTO, "Job updated successfully", HttpStatus.OK);
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
