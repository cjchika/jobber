package com.cjchika.jobber.user.service;

import com.cjchika.jobber.user.dto.UserResponseDTO;
import com.cjchika.jobber.user.mapper.UserMapper;
import com.cjchika.jobber.user.model.User;
import com.cjchika.jobber.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper){
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public List<UserResponseDTO> getUsers(){
        List<User> users = userRepository.findAll();
        return users.stream().map(user -> userMapper.toDTO(user)).toList();
    }

}
