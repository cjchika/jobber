package com.cjchika.jobber.category.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class JobCategoryRequestDTO {
    @NotNull(message = "Job ID is required")
    private UUID jobId;

    @NotNull(message = "Category ID is required")
    private UUID categoryId;

    public UUID getJobId() {
        return jobId;
    }

    public void setJobId(UUID jobId) {
        this.jobId = jobId;
    }

    public UUID getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(UUID categoryId) {
        this.categoryId = categoryId;
    }
}
