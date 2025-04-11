package com.cjchika.jobber.job.mapper;

import com.cjchika.jobber.job.dto.JobRequestDTO;
import com.cjchika.jobber.job.dto.JobResponseDTO;
import com.cjchika.jobber.job.model.Job;
import com.cjchika.jobber.job.service.JobService;
import org.springframework.stereotype.Component;

@Component
public class JobMapper {

    private JobService jobService;

    public JobMapper(JobService jobService){
        this.jobService = jobService;
    }

    public JobResponseDTO toDTO(Job job){
        JobResponseDTO jobResponseDTO = new JobResponseDTO();

        jobResponseDTO.setId(job.getId().toString());
        jobResponseDTO.s(job.getTitle());
        jobResponseDTO.setEmail(job.getDescription());
        jobResponseDTO.setRole(job.getLocation());
        jobResponseDTO.setCompanyId(job.getCompanyId());

        return  userResponseDTO;
    }

    public Job toModel(JobRequestDTO jobRequestDTO){
        Job user = new Job();

        user.setFullName(jobRequestDTO.getFullName());
        user.setEmail(jobRequestDTO.getEmail());
        user.setPassword(passwordEncoder.encode(jobRequestDTO.getPassword()));
        user.setRole(jobRequestDTO.getRole());
        user.setCompanyId(jobRequestDTO.getCompanyId());

        return user;
    }

    public LoginResponseDTO toLoginResponseDTO(Job user){
        LoginResponseDTO loginResponseDTO = new LoginResponseDTO();

        loginResponseDTO.setId(user.getId().toString());
        loginResponseDTO.setFullName(user.getFullName());
        loginResponseDTO.setEmail(user.getEmail());
        loginResponseDTO.setRole(user.getRole());
        loginResponseDTO.setCompanyId(user.getCompanyId());
        loginResponseDTO.setToken(jwtService.generateToken(user));
        loginResponseDTO.setExpiresIn(jwtService.getExpirationTime());

        return  loginResponseDTO;
    }

    public void updateModel(JobRequestDTO dto, Job user){
        if(dto.getFullName() != null && !dto.getFullName().isBlank()){
            user.setFullName(dto.getFullName());
        }
        user.setCompanyId(dto.getCompanyId());
    }
}
