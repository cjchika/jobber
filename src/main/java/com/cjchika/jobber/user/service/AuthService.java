package com.cjchika.jobber.user.service;

import com.cjchika.jobber.user.dto.UserRequestDTO;
import com.cjchika.jobber.user.dto.UserResponseDTO;
import com.cjchika.jobber.user.exception.UserException;
import com.cjchika.jobber.user.mapper.UserMapper;
import com.cjchika.jobber.user.model.User;
import com.cjchika.jobber.user.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public UserResponseDTO register(UserRequestDTO userRequestDTO){
        if(userRepository.existsByEmail(userRequestDTO.getEmail())){
           throw new UserException("Email already exists " + userRequestDTO.getEmail(), HttpStatus.BAD_REQUEST);
        }

        User newUser = userRepository.save(UserMapper.toModel(userRequestDTO));
        return UserMapper.toDTO(newUser);
    }
}
