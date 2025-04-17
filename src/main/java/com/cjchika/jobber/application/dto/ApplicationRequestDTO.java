package com.cjchika.jobber.application.dto;

import com.cjchika.jobber.application.enums.Status;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class ApplicationRequestDTO {

    @NotNull(message = "User Id is required")
    private UUID userId;

    @NotNull(message = "Job Id is required")
    private UUID jobId;

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getJobId() {
        return jobId;
    }

    public void setJobId(UUID jobId) {
        this.jobId = jobId;
    }

    @Override
    public String toString() {
        return "ApplicationRequestDTO{" +
                ", userId=" + userId +
                ", jobId=" + jobId +
                '}';
    }
}
