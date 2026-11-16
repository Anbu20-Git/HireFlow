package com.hireflow.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hireflow.entity.Application;

public interface ApplicationRepository
        extends JpaRepository<Application, Long> {

    List<Application> findByCandidate_Id(Long candidateId);

    List<Application> findByJob_Id(Long jobId);

    List<Application> findByStatus(Application.ApplicationStatus status);

    boolean existsByJob_IdAndCandidate_Id(Long jobId, Long candidateId);
    
    long countByJob_Recruiter_Id(Long recruiterId);
    
    long countByJob_Recruiter_IdAndStatus(
            Long recruiterId,
            Application.ApplicationStatus status);
}