package com.cjchika.jobber.company.mapper;

import com.cjchika.jobber.company.dto.CompanyDTO;
import com.cjchika.jobber.company.dto.CompanyResponseDTO;
import com.cjchika.jobber.company.model.Company;

public class CompanyMapper {

    public static CompanyResponseDTO toDTO(Company company){
        CompanyResponseDTO companyResponseDTO = new CompanyResponseDTO();

        companyResponseDTO.setId(company.getId());
        companyResponseDTO.setName(company.getName());
        companyResponseDTO.setDescription(company.getDescription());
        companyResponseDTO.setWebsite(company.getWebsite());

        return  companyResponseDTO;
    }

    public static Company toModel(CompanyDTO companyDTO){
        Company company = new Company();

        company.setName(companyDTO.getName());
        company.setDescription(companyDTO.getDescription());
        company.setWebsite(companyDTO.getWebsite());

        return company;
    }
}
