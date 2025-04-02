package com.cjchika.jobber.user.controller;

import com.cjchika.jobber.user.api.ApiResponse;
import com.cjchika.jobber.user.dto.UserResponseDTO;
import com.cjchika.jobber.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<ApiResponse<List<UserResponseDTO>>> getUsers(){
        List<UserResponseDTO> users = userService.getUsers();
        return ApiResponse.success(users, "Users retrieved successfully", HttpStatus.OK);
    }
}
