package com.hireflow.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hireflow.entity.Interview;

public interface InterviewRepository
        extends JpaRepository<Interview, Long> {

    List<Interview> findByApplication_Id(Long applicationId);

    List<Interview> findByStatus(Interview.InterviewStatus status);

    long countByApplication_Job_Recruiter_IdAndStatusAndInterviewDateAfter(
            Long recruiterId,
            Interview.InterviewStatus status,
            LocalDateTime currentDateTime);
}