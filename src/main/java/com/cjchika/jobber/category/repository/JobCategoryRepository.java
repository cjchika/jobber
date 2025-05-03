package com.cjchika.jobber.category.repository;

import com.cjchika.jobber.category.model.JobCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface JobCategoryRepository extends JpaRepository<JobCategory, Long> {
    boolean existsByJobIdAndCategoryId(UUID jobId, UUID categoryId);

    List<JobCategory> findByJobId(UUID jobId);

    List<JobCategory> findByCategoryId(UUID categoryId);

    @Modifying
    @Query("DELETE FROM JobCategory jc WHERE jc.job.id = :jobId AND jc.category.id = :categoryId")
    void deleteByJobIdAndCategoryId(@Param("jobId") UUID jobId, @Param("categoryId") UUID categoryId);
}
