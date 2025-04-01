package com.cjchika.jobber.user.controller;

import com.cjchika.jobber.user.dto.UserResponseDTO;
import com.cjchika.jobber.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    public ResponseEntity<List<UserResponseDTO>> getUsers(){
        List<UserResponseDTO> users = userService.getUsers();
        return ResponseEntity.ok().body(users);
    }
}
