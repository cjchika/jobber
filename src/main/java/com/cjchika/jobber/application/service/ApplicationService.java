package com.cjchika.jobber.application.service;


import com.cjchika.jobber.application.dto.ApplicationRequestDTO;
import com.cjchika.jobber.application.dto.ApplicationResponseDTO;
import com.cjchika.jobber.application.mapper.ApplicationMapper;
import com.cjchika.jobber.application.model.Application;
import com.cjchika.jobber.application.respository.ApplicationRepository;
import com.cjchika.jobber.exception.JobberException;
import com.cjchika.jobber.job.dto.JobResponseDTO;
import com.cjchika.jobber.job.dto.JobUpdateDTO;
import com.cjchika.jobber.job.mapper.JobMapper;
import com.cjchika.jobber.job.model.Job;
import com.cjchika.jobber.job.repository.JobRepository;
import com.cjchika.jobber.user.model.User;
import com.cjchika.jobber.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Transactional
@Service
public class ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    public ApplicationService(ApplicationRepository applicationRepository,
                              JobRepository jobRepository,
                              UserRepository userRepository){
        this.applicationRepository = applicationRepository;
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
    }

    public ApplicationResponseDTO postApplication(ApplicationRequestDTO appRequestDTO){
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // 3. Create and save application
        Application newJob = applicationRepository.save(ApplicationMapper.toModel(appRequestDTO));
        return ApplicationMapper.toDTO(newJob);
    }


    public JobResponseDTO updateApplication(JobUpdateDTO jobUpdateDTO, UUID jobId){
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // 1. Find the existing user by ID
        Job existingJob = jobRepository.findById(jobId)
                .orElseThrow(() -> new JobberException("Job not found", HttpStatus.NOT_FOUND));

        // 2. Verify the authenticated user owns the job
        if(!existingJob.getEmployerId().equals(currentUser.getId())){
            throw new JobberException("Unauthorized Access!", HttpStatus.FORBIDDEN);
        }

        // 3. Apply update only to non-null fields
        JobMapper.updateModel(jobUpdateDTO, existingJob);

        // 4. Validate the updated entity before saving
        if (existingJob.getSalaryMin() != null && existingJob.getSalaryMax() != null
                && existingJob.getSalaryMin() > existingJob.getSalaryMax()) {
            throw new JobberException("Minimum salary cannot exceed maximum salary", HttpStatus.BAD_REQUEST);
        }

        // 5. Save
        Job updatedJob = jobRepository.save(existingJob);

        return JobMapper.toDTO(updatedJob);
    }
}