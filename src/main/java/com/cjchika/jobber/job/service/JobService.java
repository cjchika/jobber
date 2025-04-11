package com.cjchika.jobber.job.service;

import com.cjchika.jobber.job.dto.JobResponseDTO;
import com.cjchika.jobber.exception.JobberException;
import com.cjchika.jobber.job.mapper.JobMapper;
import com.cjchika.jobber.job.model.Job;
import com.cjchika.jobber.job.repository.JobRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Transactional
@Service
public class JobService {
    private final JobRepository jobRepository;
    private final JobMapper jobMapper;

    public JobService(JobRepository jobRepository, JobMapper jobMapper){
        this.jobRepository = jobRepository;
        this.jobMapper = jobMapper;
    }

    public JobResponseDTO getUser(UUID userId){
        Job user = jobRepository.findById(userId)
                .orElseThrow(() -> new JobberException("User not found", HttpStatus.NOT_FOUND));

        return jobMapper.toDTO(user);
    }

    public List<JobResponseDTO> getUsers(){
        List<Job> users = jobRepository.findAll();
        return users.stream().map(user -> jobMapper.toDTO(user)).toList();
    }

    public JobResponseDTO updateUser(UserUpdateDTO userUpdateDTO, UUID userId){
        // 1. Find the existing user by ID
        Job existingUser = jobRepository.findById(userId)
                .orElseThrow(() -> new JobberException("User not found", HttpStatus.NOT_FOUND));

        // 2. Validate role-specific rules
//        validateRoleSpecificUpdates(existingUser, userUpdateDTO);

        // 3. Create safe copy of updates
//        UserRequestDTO safeUpdates = createSafeUpdateDTO(existingUser, userUpdateDTO);

        // 4. Apply updates
        jobMapper.updateModel(userUpdateDTO, existingUser);

        // 4. Save
        Job updatedUser = jobRepository.save(existingUser);

        return jobMapper.toDTO(updatedUser);
    }

    public void deleteUser(UUID userId){
        // 1. FIND USER
        Job user = jobRepository.findById(userId)
                .orElseThrow(() -> new JobberException("User not found", HttpStatus.NOT_FOUND));

        // 2. CHECK IF USER IS AN ADMIN
        if(user.getRole().equals(Role.ADMIN)){
            throw new JobberException("Contact super admin to perform this action", HttpStatus.FORBIDDEN);
        }

        // 3. HANDLE RELATED ENTITIES

        // 4. AUDIT LOG

        // 5. DELETE USER
        jobRepository.delete(user);

        // 6. SEND NOTIFICATION
    }

}
