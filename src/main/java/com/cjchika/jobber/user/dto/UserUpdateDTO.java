package com.cjchika.jobber.user.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public class UserUpdateDTO {
    @NotBlank
    private String fullName;

    private UUID companyId;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public UUID getCompanyId() {
        return companyId;
    }

    public void setCompanyId(UUID companyId) {
        this.companyId = companyId;
    }
}
