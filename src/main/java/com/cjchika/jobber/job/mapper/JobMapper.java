package com.cjchika.jobber.job.mapper;

import com.cjchika.jobber.job.dto.JobRequestDTO;
import com.cjchika.jobber.job.dto.JobResponseDTO;
import com.cjchika.jobber.job.dto.JobUpdateDTO;
import com.cjchika.jobber.job.enums.Status;
import com.cjchika.jobber.job.model.Job;

public class JobMapper {

    public static JobResponseDTO toDTO(Job job){
        JobResponseDTO jobResponseDTO = new JobResponseDTO();

        jobResponseDTO.setId(job.getId().toString());
        jobResponseDTO.setTitle(job.getTitle());
        jobResponseDTO.setDescription(job.getDescription());
        jobResponseDTO.setLocation(job.getLocation());
        jobResponseDTO.setSalaryMin(job.getSalaryMin());
        jobResponseDTO.setSalaryMax(job.getSalaryMax());
        jobResponseDTO.setJobType(job.getJobType());
        jobResponseDTO.setStatus(job.getStatus());
        jobResponseDTO.setCompanyId(job.getCompanyId());

        return  jobResponseDTO;
    }

    public static Job toModel(JobRequestDTO jobRequestDTO){
        Job job = new Job();

        job.setTitle(jobRequestDTO.getTitle());
        job.setDescription(jobRequestDTO.getDescription());
        job.setLocation(jobRequestDTO.getLocation());
        job.setJobType(jobRequestDTO.getJobType());
        job.setSalaryMin(jobRequestDTO.getSalaryMin());
        job.setSalaryMax(jobRequestDTO.getSalaryMax());
        job.setStatus(Status.fromValue("ACTIVE"));
        job.setCompanyId(jobRequestDTO.getCompanyId());

        return job;
    }

    public static void updateModel(JobUpdateDTO dto, Job job) {
        if (dto.getTitle() != null) {
            job.setTitle(dto.getTitle());
        }
        if (dto.getDescription() != null) {
            job.setDescription(dto.getDescription());
        }
        if (dto.getSalaryMin() != null) {
            job.setSalaryMin(dto.getSalaryMin());
        }
        if (dto.getSalaryMax() != null) {
            job.setSalaryMax(dto.getSalaryMax());
        }
        if (dto.getLocation() != null) {
            job.setLocation(dto.getLocation());
        }
        if (dto.getJobType() != null) {
            job.setJobType(dto.getJobType());
        }
        if (dto.getStatus() != null) {
            job.setStatus(dto.getStatus());
        }
    }
}
