package com.cjchika.jobber.application.dto;

import com.cjchika.jobber.application.enums.Status;

import java.util.UUID;

public class ApplicationResponseDTO {
    private String id;
    private String resumeUrl;
    private String coverLetterUrl;
    private Status status;
    private UUID userId;
    private UUID jobId;


    public String getResumeUrl() {
        return resumeUrl;
    }

    public void setResumeUrl(String resumeUrl) {
        this.resumeUrl = resumeUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}
