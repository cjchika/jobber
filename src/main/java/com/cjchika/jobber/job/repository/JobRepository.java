package com.cjchika.jobber.job.repository;

import com.cjchika.jobber.job.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JobRepository extends JpaRepository<Job, UUID> {

}
