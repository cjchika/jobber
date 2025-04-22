package com.cjchika.jobber.company.controller;

import com.cjchika.jobber.api.ApiResponse;
import com.cjchika.jobber.company.dto.CompanyDTO;
import com.cjchika.jobber.company.dto.CompanyResponseDTO;
import com.cjchika.jobber.company.service.CompanyService;
import com.cjchika.jobber.job.model.Job;
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
@RequestMapping("/api/companies")
@Tag(name = "Company", description = "Endpoints for companies")
public class CompanyController {

    @Value("${jobber.baseUrl}")
    private String baseUrl;
    private CompanyService companyService;
    private static final Logger logger = LoggerFactory.getLogger(CompanyController.class);

    public CompanyController(CompanyService companyService){
        this.companyService = companyService;
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    @Operation(summary = "Get company profile", description = "This endpoint retrieves a company's profile")
    public ResponseEntity<ApiResponse<CompanyResponseDTO>> getCompany(@PathVariable UUID id){
        try {
            logger.info("Getting company profile for ID {}:", id);
            CompanyResponseDTO company = companyService.getCompany(id);
            return ApiResponse.success(company, "Company profile retrieved successfully", HttpStatus.OK);

        } catch (Exception ex){
            logger.info("An error occurred while getting company profile: {}", ex.getMessage());

            return ApiResponse.error(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/{id}/jobs", produces = "application/json")
    @Operation(summary = "Get company jobs", description = "This endpoint retrieves all jobs posted by a company")
    public ResponseEntity<ApiResponse<List<Job>>> getEmployerJobs(@PathVariable UUID id) {
        try {
            logger.info("Getting jobs for company ID: {}", id);
            List<Job> jobs = companyService.getJobsByCompany(id);
            return ApiResponse.success(jobs, "Company jobs retrieved successfully", HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("An error occurred while getting company jobs: {}", ex.getMessage());
            return ApiResponse.error(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(produces = "application/json")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Create company", description = "This endpoint creates a new company (Only Admin)")
    public ResponseEntity<ApiResponse<CompanyResponseDTO>> postCompany(@Valid @RequestBody CompanyDTO companyDTO){
        try {
            logger.info("Creating new company");
            CompanyResponseDTO newCompany = companyService.postCompany(companyDTO);
            return ApiResponse.success(newCompany, "Company created successfully", HttpStatus.CREATED);
        } catch (Exception ex){
            logger.error("An error occurred while creating company: {}", ex.getMessage());
            return ApiResponse.error(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping(value = "/{id}", produces = "application/json")
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYER', 'ROLE_ADMIN')")
    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "Update company", description = "This endpoint updates a company's details (Only Employer or Admin)")
    public ResponseEntity<ApiResponse<CompanyResponseDTO>> updateCompany(
            @PathVariable UUID id,
            @Valid @RequestBody CompanyDTO companyDTO) {
        try {
            logger.info("Updating company with ID: {}", id);
            CompanyResponseDTO updatedCompany = companyService.updateCompany(id, companyDTO);
            return ApiResponse.success(updatedCompany, "Company updated successfully", HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("An error occurred while updating company: {}", ex.getMessage());
            return ApiResponse.error(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
