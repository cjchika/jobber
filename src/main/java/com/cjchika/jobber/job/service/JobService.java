package com.cjchika.jobber.job.service;

import com.cjchika.jobber.job.dto.JobFilterRequest;
import com.cjchika.jobber.job.dto.JobRequestDTO;
import com.cjchika.jobber.job.dto.JobResponseDTO;
import com.cjchika.jobber.exception.JobberException;
import com.cjchika.jobber.job.dto.JobUpdateDTO;
import com.cjchika.jobber.job.mapper.JobMapper;
import com.cjchika.jobber.job.model.Job;
import com.cjchika.jobber.job.repository.JobRepository;
import com.cjchika.jobber.user.enums.Role;
import com.cjchika.jobber.user.model.User;
import com.cjchika.jobber.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Transactional
@Service
public class JobService {
    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    public JobService(JobRepository jobRepository, UserRepository userRepository){
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
    }

    public JobResponseDTO postJob(JobRequestDTO jobRequestDTO){
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // 1. Verify the authenticated user matches employer in request
//        if(!jobRequestDTO.getCompanyId().equals(currentUser.getId())){
//            throw new JobberException("Unauthorized", HttpStatus.FORBIDDEN);
//        }

        // 2. Find and validate employer
         User employer = userRepository.findByCompanyId(jobRequestDTO.getCompanyId())
                    .orElseThrow(() -> new JobberException("Company not found", HttpStatus.NOT_FOUND));

         if(!employer.getRole().equals(Role.EMPLOYER)){
             throw new JobberException("Only employers can post jobs", HttpStatus.FORBIDDEN);
         }

         if(jobRequestDTO.getSalaryMin() > jobRequestDTO.getSalaryMax()){
             throw new JobberException("Minimum salary cannot exceed maximum salary", HttpStatus.BAD_REQUEST);
         }

         // 3. Create and save job
        Job newJob = jobRepository.save(JobMapper.toModel(jobRequestDTO));
        return JobMapper.toDTO(newJob);
    }

    public JobResponseDTO getJob(UUID jobId){
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new JobberException("Job not found", HttpStatus.NOT_FOUND));

        return JobMapper.toDTO(job);
    }

    public List<JobResponseDTO> getFilteredJobs(JobFilterRequest filters){
        List<Job> jobs = jobRepository.findWithFilters(
                filters.getTitle(),
                filters.getMinSalary(),
                filters.getMaxSalary(),
                filters.getLocation(),
                filters.getJobType(),
                filters.getStatus()
        );

        return jobs.stream().map(JobMapper::toDTO).collect(Collectors.toList());
    }

    public JobResponseDTO updateJob(JobUpdateDTO jobUpdateDTO, UUID jobId){
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // 1. Find the existing user by ID
        Job existingJob = jobRepository.findById(jobId)
                .orElseThrow(() -> new JobberException("Job not found", HttpStatus.NOT_FOUND));

        // 2. Verify the authenticated user owns the job
        if(!existingJob.getCompanyId().equals(currentUser.getId())){
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

    public void deleteJob(UUID jobId){
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // 1. FIND JOB
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new JobberException("Job not found", HttpStatus.NOT_FOUND));

        // 2. Check if current user is allowed to  delete the job
        boolean isAdmin = currentUser.getRole().equals(Role.ADMIN);
        boolean isOwner = currentUser.getId().equals(job.getCompanyId());

        if(!isAdmin && !isOwner){
            throw new JobberException("Unauthorized", HttpStatus.FORBIDDEN);
        }

        // 2. HANDLE RELATED ENTITIES

        // 3. AUDIT LOG

        // 4. DELETE JOB
        jobRepository.delete(job);
    }
}
