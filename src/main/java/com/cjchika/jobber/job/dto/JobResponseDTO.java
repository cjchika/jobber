package com.cjchika.jobber.job.dto;

import com.cjchika.jobber.job.enums.Role;

import java.util.UUID;

public class JobResponseDTO {
    private String id;
    private String title;
    private String email;
    private Role role;
    private UUID companyId;

}
