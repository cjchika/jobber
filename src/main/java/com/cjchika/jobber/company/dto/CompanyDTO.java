package com.cjchika.jobber.company.dto;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class CompanyDTO {
    @NotNull
    private String name;

    @NotNull
    private String description;

    @NotNull
    private String website;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}
