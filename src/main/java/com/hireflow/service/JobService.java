package com.hireflow.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.hireflow.entity.Job;
import com.hireflow.repository.JobRepository;

@Service
public class JobService {

    private final JobRepository jobRepository;

    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    public Optional<Job> getJobById(Long id) {
        return jobRepository.findById(id);
    }

    public List<Job> searchJobsByTitle(String title) {
        return jobRepository.findByJobTitleContainingIgnoreCase(title);
    }

    public List<Job> getJobsByStatus(Job.JobStatus status) {
        return jobRepository.findByStatus(status);
    }

    public List<Job> searchJobsByLocation(String location) {
        return jobRepository.findByLocationContainingIgnoreCase(location);
    }

    public Job saveJob(Job job) {
        return jobRepository.save(job);
    }

    public void deleteJob(Long id) {
        jobRepository.deleteById(id);
    }
    
    public List<Job> getJobsByRecruiter(Long recruiterId) {
        return jobRepository.findByRecruiter_Id(recruiterId);
    }
}