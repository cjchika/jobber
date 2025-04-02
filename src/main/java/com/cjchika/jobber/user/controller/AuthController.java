package com.cjchika.jobber.user.controller;

import com.cjchika.jobber.user.dto.UserRequestDTO;
import com.cjchika.jobber.user.dto.UserResponseDTO;
import com.cjchika.jobber.user.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Value("${jobber.baseUrl}")
    private String baseUrl;

    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody UserRequestDTO userRequestDTO){
//        System.out.println(userRequestDTO.toString());
        UserResponseDTO newUserResponseDTO = authService.register(userRequestDTO);

        return ResponseEntity.created(URI.create(baseUrl+newUserResponseDTO.getId())).body(newUserResponseDTO);
    }





}
