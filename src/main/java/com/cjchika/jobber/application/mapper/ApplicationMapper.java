package com.cjchika.jobber.application.mapper;


import com.cjchika.jobber.application.dto.ApplicationRequestDTO;
import com.cjchika.jobber.application.dto.ApplicationResponseDTO;
import com.cjchika.jobber.application.dto.ApplicationUpdateDTO;
import com.cjchika.jobber.application.enums.Status;
import com.cjchika.jobber.application.model.Application;

public class ApplicationMapper {

    public static ApplicationResponseDTO toDTO(Application app){
        ApplicationResponseDTO appResponseDTO = new ApplicationResponseDTO();

        appResponseDTO.setId(app.getId().toString());
        appResponseDTO.setResumeUrl(app.getResumeUrl());
        appResponseDTO.setCoverLetterUrl(app.getCoverLetterUrl());
        appResponseDTO.setStatus(app.getStatus());
        appResponseDTO.setUserId(app.getUserId());
        appResponseDTO.setJobId(app.getJobId());

        return  appResponseDTO;
    }

    public static Application toModel(ApplicationRequestDTO appRequestDTO){
        Application app = new Application();

        app.setResumeUrl(appRequestDTO.getResumeUrl());
        app.setCoverLetterUrl(appRequestDTO.getCoverLetterUrl());
        app.setStatus(Status.SUBMITTED);
        app.setUserId(appRequestDTO.getUserId());
        app.setJobId(appRequestDTO.getJobId());

        return app;
    }

    public static void updateModel(ApplicationUpdateDTO dto, Application app) {
        if (dto.getResumeUrl() != null) {
            app.setResumeUrl(dto.getResumeUrl());
        }
        if (dto.getCoverLetterUrl() != null) {
            app.setCoverLetterUrl(dto.getCoverLetterUrl());
        }
        if (dto.getStatus() != null) {
            app.setStatus(dto.getStatus());
        }
    }
}
