package com.cjchika.jobber.application.service;


import com.cjchika.jobber.application.dto.ApplicationRequestDTO;
import com.cjchika.jobber.application.dto.ApplicationResponseDTO;
import com.cjchika.jobber.application.enums.Status;
import com.cjchika.jobber.application.mapper.ApplicationMapper;
import com.cjchika.jobber.application.model.Application;
import com.cjchika.jobber.application.respository.ApplicationRepository;
import com.cjchika.jobber.exception.JobberException;
import com.cjchika.jobber.job.model.Job;
import com.cjchika.jobber.job.repository.JobRepository;
import com.cjchika.jobber.user.model.User;
import com.cjchika.jobber.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
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

        // Verify job exists
        Job job = jobRepository.findById(appRequestDTO.getJobId())
                .orElseThrow(() -> new JobberException("Job not found", HttpStatus.NOT_FOUND));

        // Verify user exists
        User user = userRepository.findById(appRequestDTO.getUserId())
                .orElseThrow(() -> new JobberException("User not found", HttpStatus.NOT_FOUND));

        // Verify user has a resume
        if(user.getResumeUrl() == null){
            throw new JobberException("User must upload a resume before applying", HttpStatus.BAD_REQUEST);
        }

        Application app = new Application();
        app.setUserId(user.getId());
        app.setJobId(job.getId());
        app.setResumeUrl(user.getResumeUrl());
        app.setStatus(Status.SUBMITTED);

        // 3. Create and save application
        Application newApp = applicationRepository.save(app);
        return ApplicationMapper.toDTO(newApp);
    }

    public List<ApplicationResponseDTO> getUserApplications(UUID userId){
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Verify requested user matches authenticated user
        if(!currentUser.getId().equals(userId)){
            throw new JobberException("Unauthorized access to applications", HttpStatus.FORBIDDEN);
        }

        List<Application> applications = applicationRepository.findByUserId(userId);
        return  applications.stream().map(ApplicationMapper::toDTO).toList();
    }

    public List<ApplicationResponseDTO> getJobApplications(UUID jobId){
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new JobberException("Job not found", HttpStatus.NOT_FOUND));

        // Verify current user is the job owner
        if(!job.getCompanyId().equals(currentUser.getId())){
            throw new JobberException("Unauthorized access to applications", HttpStatus.FORBIDDEN);
        }

        List<Application> applications = applicationRepository.findByJobId(jobId);
        return  applications.stream().map(ApplicationMapper::toDTO).toList();
    }


    public ApplicationResponseDTO updateApplicationStatus(UUID applicationId, Status status){
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Find the existing application by ID
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new JobberException("Application not found", HttpStatus.NOT_FOUND));

        Job job = jobRepository.findById(application.getJobId())
                .orElseThrow(() -> new JobberException("Job not found", HttpStatus.NOT_FOUND));

        // Verify the authenticated user owns the job
        if(!job.getCompanyId().equals(currentUser.getId())){
            throw new JobberException("Unauthorized to update application!", HttpStatus.FORBIDDEN);
        }

        application.setStatus(status);
        Application updatedApplication = applicationRepository.save(application);
        return ApplicationMapper.toDTO(updatedApplication);
    }
}