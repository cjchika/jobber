package com.cjchika.jobber.company.repository;

import com.cjchika.jobber.company.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CompanyRepository extends JpaRepository<Company, UUID> {
    Optional<Company> findByName(String name);
    Optional<Company> findByWebsite(String website);
    boolean existsByName(String name);
    boolean existsByWebsite(String website);
    boolean existsByNameAndIdNot(String name, UUID id);
    boolean existsByWebsiteAndIdNot(String website, UUID id);
}
