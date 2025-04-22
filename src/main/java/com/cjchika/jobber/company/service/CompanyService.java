package com.cjchika.jobber.company.service;

import com.cjchika.jobber.company.dto.CompanyDTO;
import com.cjchika.jobber.company.dto.CompanyResponseDTO;
import com.cjchika.jobber.company.mapper.CompanyMapper;
import com.cjchika.jobber.company.model.Company;
import com.cjchika.jobber.company.repository.CompanyRepository;
import com.cjchika.jobber.exception.JobberException;
import com.cjchika.jobber.job.model.Job;
import com.cjchika.jobber.job.repository.JobRepository;
import com.cjchika.jobber.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Transactional
@Service
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;

    public CompanyService(CompanyRepository companyRepository,
                          UserRepository userRepository,
                          JobRepository jobRepository){
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
    }

    public CompanyResponseDTO getCompany(UUID companyId){
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new JobberException("Company not found", HttpStatus.NOT_FOUND));

        return CompanyMapper.toDTO(company);
    }

    public CompanyResponseDTO postCompany(CompanyDTO companyDTO){
        // Check if company already exists by name or website
        if(companyRepository.existsByName(companyDTO.getName())){
            throw new JobberException("Company with this name already exists ", HttpStatus.BAD_REQUEST);
        }

        if(companyRepository.existsByWebsite(companyDTO.getWebsite())){
            throw new JobberException("Company with this website already exists ", HttpStatus.BAD_REQUEST);
        }

        Company savedCompany = companyRepository.save(CompanyMapper.toModel(companyDTO));

        return CompanyMapper.toDTO(savedCompany);
    }

    public CompanyResponseDTO updateCompany(UUID companyId, CompanyDTO companyDTO){
        Company existingCompany = companyRepository.findById(companyId)
                .orElseThrow(() -> new JobberException("Company not found", HttpStatus.NOT_FOUND));

        // Check if another company already exists by name or website
        if(companyRepository.existsByNameAndIdNot(companyDTO.getName(), companyId)){
            throw new JobberException("Another company with this name already exists", HttpStatus.BAD_REQUEST);
        }

        if(companyRepository.existsByWebsiteAndIdNot(companyDTO.getWebsite(), companyId)){
            throw new JobberException("Another company with this website already exists", HttpStatus.BAD_REQUEST);
        }

        // Update the company details
        existingCompany.setName(companyDTO.getName());
        existingCompany.setDescription(companyDTO.getDescription());
        existingCompany.setWebsite(companyDTO.getWebsite());

        Company updatedCompany = companyRepository.save(existingCompany);
        return CompanyMapper.toDTO(updatedCompany);
    }

    public List<Job> getJobsByCompany(UUID companyId){
        if(!companyRepository.existsById(companyId)){
            throw new JobberException("Company not found", HttpStatus.NOT_FOUND);
        }

        return jobRepository.findByCompanyId(companyId);
    }

}
