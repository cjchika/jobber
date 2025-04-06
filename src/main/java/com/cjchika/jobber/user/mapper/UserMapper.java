package com.cjchika.jobber.user.mapper;

import com.cjchika.jobber.user.dto.LoginResponseDTO;
import com.cjchika.jobber.user.dto.RegisterRequestDTO;
import com.cjchika.jobber.user.dto.UserResponseDTO;
import com.cjchika.jobber.user.model.User;
import com.cjchika.jobber.user.service.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserMapper(PasswordEncoder passwordEncoder, JwtService jwtService){
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public UserResponseDTO toDTO(User user){
        UserResponseDTO userResponseDTO = new UserResponseDTO();

        userResponseDTO.setId(user.getId().toString());
        userResponseDTO.setFullName(user.getFullName());
        userResponseDTO.setEmail(user.getEmail());
        userResponseDTO.setRole(user.getRole());
        userResponseDTO.setCompanyId(user.getCompanyId());

        return  userResponseDTO;
    }

    public User toModel(RegisterRequestDTO registerRequestDTO){
        User user = new User();

        user.setFullName(registerRequestDTO.getFullName());
        user.setEmail(registerRequestDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
        user.setRole(registerRequestDTO.getRole());
        user.setCompanyId(registerRequestDTO.getCompanyId());

        return user;
    }

    public LoginResponseDTO toLoginResponseDTO(User user){
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
}
