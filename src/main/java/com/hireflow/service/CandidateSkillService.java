package com.hireflow.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hireflow.entity.CandidateSkill;
import com.hireflow.repository.CandidateSkillRepository;

@Service
public class CandidateSkillService {

    private final CandidateSkillRepository candidateSkillRepository;

    public CandidateSkillService(
            CandidateSkillRepository candidateSkillRepository) {
        this.candidateSkillRepository = candidateSkillRepository;
    }

    public List<CandidateSkill> getSkillsByCandidate(Long candidateId) {
        return candidateSkillRepository.findByCandidate_Id(candidateId);
    }

    public boolean skillAlreadyAssigned(Long candidateId, Long skillId) {
        return candidateSkillRepository
                .existsByCandidate_IdAndSkill_Id(candidateId, skillId);
    }

    public CandidateSkill saveCandidateSkill(CandidateSkill candidateSkill) {
        return candidateSkillRepository.save(candidateSkill);
    }

    public void deleteCandidateSkill(Long id) {
        candidateSkillRepository.deleteById(id);
    }
}