package com.cjchika.jobber.job.dto;

import com.cjchika.jobber.job.enums.JobType;
import com.cjchika.jobber.job.enums.Status;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.UUID;

public class JobRequestDTO {
    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Salary Minimum is required")
    @DecimalMin(value = "0.0", message = "Salary must be positive")
    private Double salaryMin;

    @NotNull(message = "Salary Maximum is required")
    private Double salaryMax;

    @NotBlank(message = "Location is required")
    private String location;

    @NotNull(message = "Job Type is required")
//    @Pattern(regexp = "FULL_TIME|PART_TIME|CONTRACT|INTERNSHIP",
//            flags = Pattern.Flag.CASE_INSENSITIVE,
//            message = "Invalid job type")
//    private String jobTypeString;
    private JobType jobType;

    private Status status;

    @NotNull(message = "Employer Id is required")
    private UUID employerId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public UUID getEmployerId() {
        return employerId;
    }

    public void setEmployerId(UUID employerId) {
        this.employerId = employerId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public JobType getJobType() {
        return jobType;
    }

    public void setJobType(JobType jobType) {
        this.jobType = jobType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getSalaryMax() {
        return salaryMax;
    }

    public void setSalaryMax(Double salaryMax) {
        this.salaryMax = salaryMax;
    }

    public Double getSalaryMin() {
        return salaryMin;
    }

    public void setSalaryMin(Double salaryMin) {
        this.salaryMin = salaryMin;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "JobRequestDTO{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", salaryMin=" + salaryMin +
                ", salaryMax=" + salaryMax +
                ", location='" + location + '\'' +
                ", jobType=" + jobType +
                ", status=" + status +
                ", employerId=" + employerId +
                '}';
    }
}
