package com.cjchika.jobber.category.mapper;

import com.cjchika.jobber.category.dto.JobCategoryResponseDTO;
import com.cjchika.jobber.category.model.JobCategory;

public class JobCategoryMapper {
    public static JobCategoryResponseDTO toDTO(JobCategory jobCategory) {
        JobCategoryResponseDTO dto = new JobCategoryResponseDTO();
        dto.setJobId(jobCategory.getJob().getId());
        dto.setCategoryId(jobCategory.getCategory().getId());
        dto.setJobTitle(jobCategory.getJob().getTitle());
        dto.setCategoryName(jobCategory.getCategory().getName());
        return dto;
    }
}