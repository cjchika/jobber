package com.cjchika.jobber.job.repository;

import com.cjchika.jobber.job.enums.JobType;
import com.cjchika.jobber.job.enums.Status;
import com.cjchika.jobber.job.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface JobRepository extends JpaRepository<Job, UUID> {
//    Optional<Job> findById(UUID id);

    List<Job> findByEmployerId(UUID companyId);

    @Query("SELECT j FROM Job j WHERE " +
            "(:title IS NULL OR j.title LIKE %:title%) AND " +
            "(:minSalary IS NULL OR j.salaryMin >= :minSalary) AND " +
            "(:maxSalary IS NULL OR j.salaryMax <= :maxSalary) AND " +
            "(:location IS NULL OR j.location = :location) AND " +
            "(:jobType IS NULL OR j.jobType = :jobType) AND " +
            "(:status IS NULL OR j.status = :status)")
    List<Job> findWithFilters(
            @Param("title") String title,
            @Param("minSalary") Double minSalary,
            @Param("maxSalary") Double maxSalary,
            @Param("location") String location,
            @Param("jobType") JobType jobType,
            @Param("status") Status status
    );
}
