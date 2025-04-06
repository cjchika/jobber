package com.cjchika.jobber.user.service;

import com.cjchika.jobber.user.dto.LoginRequestDTO;
import com.cjchika.jobber.user.dto.LoginResponseDTO;
import com.cjchika.jobber.user.dto.RegisterRequestDTO;
import com.cjchika.jobber.user.dto.UserResponseDTO;
import com.cjchika.jobber.user.exception.UserException;
import com.cjchika.jobber.user.mapper.UserMapper;
import com.cjchika.jobber.user.model.User;
import com.cjchika.jobber.user.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository,
                       AuthenticationManager authenticationManager,
                       UserMapper userMapper
                       ){
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.userMapper = userMapper;
    }

    public UserResponseDTO register(RegisterRequestDTO registerRequestDTO){
        if(userRepository.existsByEmail(registerRequestDTO.getEmail())){
           throw new UserException("Email already exists " + registerRequestDTO.getEmail(), HttpStatus.BAD_REQUEST);
        }

        User newUser = userRepository.save(userMapper.toModel(registerRequestDTO));
        return userMapper.toDTO(newUser);
    }

    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(), loginRequestDTO.getPassword())
        );

       User user = userRepository.findByEmail(loginRequestDTO.getEmail())
               .orElseThrow(() -> new UserException("User not found", HttpStatus.NOT_FOUND));

        return userMapper.toLoginResponseDTO(user);
    }
}
