package com.cjchika.jobber.user.mapper;

import com.cjchika.jobber.user.dto.UserRequestDTO;
import com.cjchika.jobber.user.dto.UserResponseDTO;
import com.cjchika.jobber.user.model.User;

public class UserMapper {
    public static UserResponseDTO toDTO(User user){
        UserResponseDTO userResponseDTO = new UserResponseDTO();

        userResponseDTO.setId(user.getId().toString());
        userResponseDTO.setFullName(user.getFullName());
        userResponseDTO.setEmail(user.getEmail());
        userResponseDTO.setRole(user.getRole());
        userResponseDTO.setCompanyId(user.getCompanyId());

        return  userResponseDTO;
    }

    public static User toModel(UserRequestDTO userRequestDTO){
        User user = new User();

        user.setFullName(userRequestDTO.getFullName());
        user.setEmail(user.getEmail());
        user.setPassword(user.getPassword());
        user.setRole(userRequestDTO.getRole());
        user.setCompanyId(userRequestDTO.getCompanyId());

        return user;
    }
}
