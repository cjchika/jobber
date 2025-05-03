package com.cjchika.jobber.category.repository;

import com.cjchika.jobber.category.model.JobCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JobCategoryRepository extends JpaRepository<JobCategory, Long> {
    boolean existsByJobIdAndCategoryId(UUID jobId, UUID categoryId);
}
