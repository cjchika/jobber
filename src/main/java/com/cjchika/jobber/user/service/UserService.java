package com.cjchika.jobber.user.service;

import com.cjchika.jobber.user.dto.UserRequestDTO;
import com.cjchika.jobber.user.dto.UserResponseDTO;
import com.cjchika.jobber.user.enums.Role;
import com.cjchika.jobber.user.exception.UserException;
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
                .orElseThrow(() -> new UserException("User not found", HttpStatus.NOT_FOUND));

        return userMapper.toDTO(user);
    }

    public List<UserResponseDTO> getUsers(){
        List<User> users = userRepository.findAll();
        return users.stream().map(user -> userMapper.toDTO(user)).toList();
    }

    public UserResponseDTO updateUser(UserRequestDTO userRequestDTO, UUID userId){
        // 1. Find the existing user by ID
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserException("User not found", HttpStatus.NOT_FOUND));

        // 2. Check if email is being changed to one that's already taken by another user
        if(!existingUser.getEmail().equals(userRequestDTO.getEmail()) && userRepository.existsByEmail(userRequestDTO.getEmail())){
            throw new UserException("Email already in use", HttpStatus.BAD_REQUEST);
        }

        // 3. Update the existing user with new data
        userMapper.updateModel(userRequestDTO, existingUser);

        // 4. Save the updated user
        User updatedUser = userRepository.save(existingUser);

        return userMapper.toDTO(updatedUser);
    }


    public Boolean deleteUser(UUID userId){
        // 1. FIND USER
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException("User not found", HttpStatus.NOT_FOUND));

        // 2. CHECK IF USER IS AN ADMIN
        if(user.getRole().equals(Role.ADMIN)){
            throw new UserException("Contact super admin to perform this action", HttpStatus.FORBIDDEN);
        }

        // 3. HANDLE RELATED ENTITIES

        // 4. AUDIT LOG

        // 5. DELETE USER
        userRepository.delete(user);

        // 6. SEND NOTIFICATION

        return true;
    }

}
