package com.cjchika.jobber.user.service;

import com.cjchika.jobber.company.model.Company;
import com.cjchika.jobber.company.repository.CompanyRepository;
import com.cjchika.jobber.user.dto.LoginRequestDTO;
import com.cjchika.jobber.user.dto.LoginResponseDTO;
import com.cjchika.jobber.user.dto.UserRequestDTO;
import com.cjchika.jobber.user.dto.UserResponseDTO;
import com.cjchika.jobber.user.enums.Role;
import com.cjchika.jobber.exception.JobberException;
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
    private final CompanyRepository companyRepository;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository,
                       CompanyRepository companyRepository,
                       AuthenticationManager authenticationManager,
                       UserMapper userMapper
                       ){
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.authenticationManager = authenticationManager;
        this.userMapper = userMapper;
    }

    public UserResponseDTO register(UserRequestDTO userRequestDTO){
        //  Check if email exists
        if(userRepository.existsByEmail(userRequestDTO.getEmail())){
           throw new JobberException("Email already exists " + userRequestDTO.getEmail(), HttpStatus.BAD_REQUEST);
        }

        // Handle EMPLOYER role specifically
        if(userRequestDTO.getRole() == Role.EMPLOYER){
            handleEmployerRegistration(userRequestDTO);
        }

        User newUser = userRepository.save(userMapper.toModel(userRequestDTO));
        return userMapper.toDTO(newUser);
    }

    private void handleEmployerRegistration(UserRequestDTO userRequestDTO) {
        // Case A: Joining existing company
        if(userRequestDTO.getCompanyId() != null){
            // Verify company exists
            companyRepository.findById(userRequestDTO.getCompanyId())
                    .orElseThrow(() -> new JobberException("Company not found with id " + userRequestDTO.getCompanyId(), HttpStatus.BAD_REQUEST));
        }

        // Case B: Creating new company
        if(userRequestDTO.getCompany() != null){
            UserRequestDTO.CompanyDTO companyDTO = userRequestDTO.getCompany();

            // Check if company already exists by name or website
            if(companyRepository.existsByName(companyDTO.getName())){
                throw new JobberException("Company with this name already exists ", HttpStatus.BAD_REQUEST);
            }

            if(companyRepository.existsByWebsite(companyDTO.getWebsite())){
                throw new JobberException("Company with this website already exists ", HttpStatus.BAD_REQUEST);
            }

            // Create new company
            Company newCompany = new Company();
            newCompany.setName(companyDTO.getName());
            newCompany.setWebsite(companyDTO.getWebsite());
            newCompany.setDescription(companyDTO.getDescription());

            Company savedCompany = companyRepository.save(newCompany);
            userRequestDTO.setCompanyId(savedCompany.getId());
        } else {
            // Neither companyId nor company details provided
            throw new JobberException("Employers must belong to a company", HttpStatus.BAD_REQUEST);
        }
    }

    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(), loginRequestDTO.getPassword())
        );

       User user = userRepository.findByEmail(loginRequestDTO.getEmail())
               .orElseThrow(() -> new JobberException("User not found", HttpStatus.NOT_FOUND));

        return userMapper.toLoginResponseDTO(user);
    }
}
