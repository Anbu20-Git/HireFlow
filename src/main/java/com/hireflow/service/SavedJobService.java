package com.hireflow.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hireflow.entity.SavedJob;
import com.hireflow.repository.SavedJobRepository;

@Service
public class SavedJobService {

    private final SavedJobRepository savedJobRepository;

    public SavedJobService(SavedJobRepository savedJobRepository) {
        this.savedJobRepository = savedJobRepository;
    }

    public List<SavedJob> getSavedJobsByCandidate(Long candidateId) {
        return savedJobRepository.findByCandidate_Id(candidateId);
    }

    public boolean isJobSaved(Long candidateId, Long jobId) {
        return savedJobRepository
                .existsByCandidate_IdAndJob_Id(candidateId, jobId);
    }

    public SavedJob saveJob(SavedJob savedJob) {
        return savedJobRepository.save(savedJob);
    }

    @Transactional
    public void removeSavedJob(Long candidateId, Long jobId) {
        savedJobRepository.deleteByCandidate_IdAndJob_Id(candidateId, jobId);
    }
}