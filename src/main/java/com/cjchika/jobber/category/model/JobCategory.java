package com.cjchika.jobber.category.model;

import com.cjchika.jobber.job.model.Job;
import jakarta.persistence.*;

@Entity
@Table(name = "job_categories")
public class JobCategory {
    @EmbeddedId
    private JobCategoryId id;

    @ManyToOne
    @MapsId("jobId")
    @JoinColumn(name = "job_id")
    private Job job;

    @ManyToOne
    @MapsId("categoryId")
    @JoinColumn(name = "category_id")
    private Category category;

    // Getters and Setters
    public JobCategoryId getId() {
        return id;
    }

    public void setId(JobCategoryId id) {
        this.id = id;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
