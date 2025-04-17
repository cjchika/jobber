package com.cjchika.jobber.application.respository;

import com.cjchika.jobber.application.model.Application;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ApplicationRepository extends JpaRepository<Application, UUID> {
    List<Application> findByUserId(UUID userId);
    List<Application> findByJobId(UUID jobId);
}
