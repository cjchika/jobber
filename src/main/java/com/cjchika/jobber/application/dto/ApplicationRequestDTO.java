package com.cjchika.jobber.application.dto;

import com.cjchika.jobber.application.enums.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class ApplicationRequestDTO {
    @NotBlank(message = "ResumeURL is required")
    private String resumeUrl;

    private String coverLetterUrl;

    private Status status;

    @NotNull(message = "User Id is required")
    private UUID userId;

    @NotNull(message = "Job Id is required")
    private UUID jobId;

    public String getResumeUrl() {
        return resumeUrl;
    }

    public void setResumeUrl(String resumeUrl) {
        this.resumeUrl = resumeUrl;
    }

    public String getCoverLetterUrl() {
        return coverLetterUrl;
    }

    public void setCoverLetterUrl(String coverLetterUrl) {
        this.coverLetterUrl = coverLetterUrl;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

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
                "resumeUrl='" + resumeUrl + '\'' +
                ", coverLetterUrl='" + coverLetterUrl + '\'' +
                ", status=" + status +
                ", userId=" + userId +
                ", jobId=" + jobId +
                '}';
    }
}
