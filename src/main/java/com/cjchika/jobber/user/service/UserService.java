package com.cjchika.jobber.user.service;

import com.cjchika.jobber.user.dto.UserResponseDTO;
import com.cjchika.jobber.user.mapper.UserMapper;
import com.cjchika.jobber.user.model.User;
import com.cjchika.jobber.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public List<UserResponseDTO> getUsers(){
        List<User> users = userRepository.findAll();
        List<UserResponseDTO> userResponseDTOS = users.stream().map(user -> UserMapper.toDTO(user)).toList();
        return userResponseDTOS;
    }

}
