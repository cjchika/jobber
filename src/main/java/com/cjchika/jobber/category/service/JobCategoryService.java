package com.cjchika.jobber.category.service;

import com.cjchika.jobber.category.dto.JobCategoryRequestDTO;
import com.cjchika.jobber.category.dto.JobCategoryResponseDTO;
import com.cjchika.jobber.category.mapper.JobCategoryMapper;
import com.cjchika.jobber.category.model.Category;
import com.cjchika.jobber.category.model.JobCategory;
import com.cjchika.jobber.category.model.JobCategoryId;
import com.cjchika.jobber.category.repository.CategoryRepository;
import com.cjchika.jobber.category.repository.JobCategoryRepository;
import com.cjchika.jobber.exception.JobberException;
import com.cjchika.jobber.job.model.Job;
import com.cjchika.jobber.job.repository.JobRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class JobCategoryService {
    private final JobCategoryRepository jobCategoryRepository;
    private final JobRepository jobRepository;
    private final CategoryRepository categoryRepository;

    public JobCategoryService(JobCategoryRepository jobCategoryRepository,
                              JobRepository jobRepository,
                              CategoryRepository categoryRepository){
        this.jobCategoryRepository = jobCategoryRepository;
        this.jobRepository = jobRepository;
        this.categoryRepository = categoryRepository;
    }

    public JobCategoryResponseDTO addCategoryToJob(JobCategoryRequestDTO requestDTO){
        Job job = jobRepository.findById((requestDTO.getJobId()))
                .orElseThrow(() -> new JobberException("Job not found", HttpStatus.NOT_FOUND));

        Category category = categoryRepository.findById(requestDTO.getCategoryId())
                .orElseThrow(() -> new JobberException("Category not found", HttpStatus.NOT_FOUND));

        if(jobCategoryRepository.existsByJobIdAndCategoryId(job.getId(), category.getId())){
            throw new JobberException("This job is already associated with the category", HttpStatus.CONFLICT);
        }

        JobCategory jobCategory = new JobCategory();
        jobCategory.setJob(job);
        jobCategory.setCategory(category);
        jobCategory.setId(new JobCategoryId(job.getId(), category.getId()));

        jobCategory = jobCategoryRepository.save(jobCategory);
        return JobCategoryMapper.toDTO(jobCategory);
    }

    public List<JobCategoryResponseDTO> getCategoriesForJob(UUID jobId){
        if(!jobRepository.existsById(jobId)){
            throw new JobberException("Job not found", HttpStatus.NOT_FOUND);
        }

        return jobCategoryRepository.findByJobId(jobId).stream()
                .map(JobCategoryMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<JobCategoryResponseDTO> getJobsForCategory(UUID categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new JobberException("Category not found", HttpStatus.NOT_FOUND);
        }

        return jobCategoryRepository.findByCategoryId(categoryId).stream()
                .map(JobCategoryMapper::toDTO)
                .collect(Collectors.toList());
    }

    public void removeCategoryFromJob(UUID jobId, UUID categoryId){
        if(!jobCategoryRepository.existsByJobIdAndCategoryId(jobId, categoryId)){
            throw new JobberException("Job-Category relationship not found", HttpStatus.NOT_FOUND);
        }

        jobCategoryRepository.deleteByJobIdAndCategoryId(jobId, categoryId);
    }
}
