package com.cjchika.jobber.job.repository;

import com.cjchika.jobber.job.model.Job;
import com.cjchika.jobber.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JobRepository extends JpaRepository<Job, UUID> {
//    Optional<Job> findById(UUID id);
}
