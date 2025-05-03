package com.cjchika.jobber.category.model;

import java.io.Serializable;
import java.util.UUID;
import jakarta.persistence.Embeddable;

@Embeddable
public class JobCategoryId implements Serializable {
    private UUID jobId;
    private UUID categoryId;

    // Constructors
    public JobCategoryId() {}

    public JobCategoryId(UUID jobId, UUID categoryId) {
        this.jobId = jobId;
        this.categoryId = categoryId;
    }

    // Getters and Setters
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

    // Equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JobCategoryId)) return false;
        JobCategoryId that = (JobCategoryId) o;
        return jobId.equals(that.jobId) && categoryId.equals(that.categoryId);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(jobId, categoryId);
    }
}