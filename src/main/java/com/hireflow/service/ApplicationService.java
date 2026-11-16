package com.hireflow.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hireflow.entity.Application;
import com.hireflow.repository.ApplicationRepository;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;

    public ApplicationService(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    public List<Application> getAllApplications() {
        return applicationRepository.findAll();
    }

    public Optional<Application> getApplicationById(Long id) {
        return applicationRepository.findById(id);
    }

    public List<Application> getApplicationsByCandidate(Long candidateId) {
        return applicationRepository.findByCandidate_Id(candidateId);
    }

    public List<Application> getApplicationsByJob(Long jobId) {
        return applicationRepository.findByJob_Id(jobId);
    }

    public List<Application> getApplicationsByStatus(
            Application.ApplicationStatus status) {
        return applicationRepository.findByStatus(status);
    }

    public boolean hasCandidateApplied(Long jobId, Long candidateId) {
        return applicationRepository
                .existsByJob_IdAndCandidate_Id(jobId, candidateId);
    }

    public long getTotalApplicantsByRecruiter(Long recruiterId) {
        return applicationRepository
                .countByJob_Recruiter_Id(recruiterId);
    }
    
    public long getShortlistedCountByRecruiter(Long recruiterId) {
        return applicationRepository.countByJob_Recruiter_IdAndStatus(
                recruiterId,
                Application.ApplicationStatus.SHORTLISTED);
    }

    public Application saveApplication(Application application) {
        return applicationRepository.save(application);
    }

    @Transactional
    public void deleteApplication(Long id) {
        applicationRepository.deleteById(id);
    }
}