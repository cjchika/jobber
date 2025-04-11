package com.cjchika.jobber.job.dto;

import com.cjchika.jobber.job.enums.JobType;
import com.cjchika.jobber.job.enums.Status;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Pattern;

public class JobUpdateDTO {
    private String title;
    private String description;

    @DecimalMin(value = "0.0", message = "Salary must be positive")
    private Double salaryMin;

    private Double salaryMax;
    private String location;
    private JobType jobType;
    private Status status;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getSalaryMin() {
        return salaryMin;
    }

    public void setSalaryMin(Double salaryMin) {
        this.salaryMin = salaryMin;
    }

    public Double getSalaryMax() {
        return salaryMax;
    }

    public void setSalaryMax(Double salaryMax) {
        this.salaryMax = salaryMax;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public JobType getJobType() {
        return jobType;
    }

    public void setJobType(JobType jobType) {
        this.jobType = jobType;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "JobUpdateDTO{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", salaryMin=" + salaryMin +
                ", salaryMax=" + salaryMax +
                ", location='" + location + '\'' +
                ", jobType=" + jobType +
                ", status=" + status +
                '}';
    }
}