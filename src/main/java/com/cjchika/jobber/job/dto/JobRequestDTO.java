package com.cjchika.jobber.job.dto;

import com.cjchika.jobber.job.enums.JobType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class JobRequestDTO {
    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Salary Minimum is required")
    private String salaryMin;

    @NotBlank(message = "Salary Maximum is required")
    private String salaryMax;

    @NotBlank(message = "Location is required")
    private String location;

    @NotNull(message = "Job Type is required")
    private JobType jobType;

    @NotNull(message = "Job Status is required")
    private JobType jobStatus;

    private UUID employerId;

}
