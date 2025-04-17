package com.cjchika.jobber.application.dto;

import com.cjchika.jobber.application.enums.Status;

public class ApplicationUpdateDTO {
    private String resumeUrl;
    private String coverLetterUrl;
    private Status status;

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
}
