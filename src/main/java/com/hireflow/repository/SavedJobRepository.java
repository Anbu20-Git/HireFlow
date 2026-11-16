package com.hireflow.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hireflow.entity.SavedJob;

public interface SavedJobRepository extends JpaRepository<SavedJob, Long> {

    List<SavedJob> findByCandidate_Id(Long candidateId);

    boolean existsByCandidate_IdAndJob_Id(Long candidateId, Long jobId);

    void deleteByCandidate_IdAndJob_Id(Long candidateId, Long jobId);
}