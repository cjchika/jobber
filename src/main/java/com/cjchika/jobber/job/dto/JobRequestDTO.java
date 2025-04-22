package com.cjchika.jobber.job.dto;

import com.cjchika.jobber.job.enums.JobType;
import com.cjchika.jobber.job.enums.Status;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

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
    private JobType jobType;

    private Status status;

    @NotNull(message = "Company Id is required")
    private UUID companyId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public UUID getCompanyId() {
        return companyId;
    }

    public void setCompanyId(UUID companyId) {
        this.companyId = companyId;
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
                ", companyId=" + companyId +
                '}';
    }
}
