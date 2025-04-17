package com.cjchika.jobber.application.respository;

import com.cjchika.jobber.application.model.Application;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ApplicationRepository extends JpaRepository<Application, UUID> {
}
