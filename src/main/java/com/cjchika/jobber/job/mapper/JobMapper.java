package com.cjchika.jobber.job.mapper;

import com.cjchika.jobber.job.dto.JobRequestDTO;
import com.cjchika.jobber.job.dto.JobResponseDTO;
import com.cjchika.jobber.job.model.Job;
import com.cjchika.jobber.job.service.JobService;
import org.springframework.stereotype.Component;

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
        jobResponseDTO.setEmployerId(job.getEmployerId());

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
        job.setStatus(jobRequestDTO.getJobStatus());
        job.setEmployerId(jobRequestDTO.getEmployerId());

        return job;
    }

//    public void updateModel(JobRequestDTO dto, Job user){
//        if(dto.getFullName() != null && !dto.getFullName().isBlank()){
//            user.setFullName(dto.getFullName());
//        }
//        user.setCompanyId(dto.getCompanyId());
//    }
}
