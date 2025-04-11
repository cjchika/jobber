package com.cjchika.jobber.user.service;

import com.cjchika.jobber.user.dto.UserRequestDTO;
import com.cjchika.jobber.user.dto.UserResponseDTO;
import com.cjchika.jobber.user.dto.UserUpdateDTO;
import com.cjchika.jobber.user.enums.Role;
import com.cjchika.jobber.exception.JobberException;
import com.cjchika.jobber.user.mapper.UserMapper;
import com.cjchika.jobber.user.model.User;
import com.cjchika.jobber.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Transactional
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper){
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserResponseDTO getUser(UUID userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new JobberException("User not found", HttpStatus.NOT_FOUND));

        return userMapper.toDTO(user);
    }

    public List<UserResponseDTO> getUsers(){
        List<User> users = userRepository.findAll();
        return users.stream().map(user -> userMapper.toDTO(user)).toList();
    }

    public UserResponseDTO updateUser(UserUpdateDTO userUpdateDTO, UUID userId){
        // 1. Find the existing user by ID
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new JobberException("User not found", HttpStatus.NOT_FOUND));

        // 2. Validate role-specific rules
        validateRoleSpecificUpdates(existingUser, userUpdateDTO);

        // 3. Create safe copy of updates
        UserRequestDTO safeUpdates = createSafeUpdateDTO(existingUser, userUpdateDTO);

        // 4. Apply updates
        userMapper.updateModel(safeUpdates, existingUser);

        // 4. Save
        User updatedUser = userRepository.save(existingUser);

        return userMapper.toDTO(updatedUser);
    }

    private void validateRoleSpecificUpdates(User existingUser, UserUpdateDTO updates){
        // Prevent non-employers from setting companyId
        if(!existingUser.getRole().equals(Role.EMPLOYER)){
            if(updates.getCompanyId() != null){
                throw new JobberException("Only employers can have company association", HttpStatus.FORBIDDEN);
            }
        }

        // Prevent employers from removing their companyId
        if(existingUser.getRole().equals(Role.EMPLOYER) && existingUser.getCompanyId() !=  null && updates.getCompanyId() == null){
            throw new JobberException("Employers cannot remove company association", HttpStatus.FORBIDDEN);
        }
    }

    private UserRequestDTO createSafeUpdateDTO(User existingUser, UserUpdateDTO input) {
        UserRequestDTO safeDTO = new UserRequestDTO();
        safeDTO.setFullName(input.getFullName());

        // Only allow companyId updates for employers
        if(existingUser.getRole().equals(Role.EMPLOYER)){
            safeDTO.setCompanyId(input.getCompanyId());
        }

        return safeDTO;
    }


    public void deleteUser(UUID userId){
        // 1. FIND USER
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new JobberException("User not found", HttpStatus.NOT_FOUND));

        // 2. CHECK IF USER IS AN ADMIN
        if(user.getRole().equals(Role.ADMIN)){
            throw new JobberException("Contact super admin to perform this action", HttpStatus.FORBIDDEN);
        }

        // 3. HANDLE RELATED ENTITIES

        // 4. AUDIT LOG

        // 5. DELETE USER
        userRepository.delete(user);

        // 6. SEND NOTIFICATION
    }

}
