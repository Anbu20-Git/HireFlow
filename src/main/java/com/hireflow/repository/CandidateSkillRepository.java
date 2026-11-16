package com.hireflow.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hireflow.entity.CandidateSkill;

public interface CandidateSkillRepository
        extends JpaRepository<CandidateSkill, Long> {

    List<CandidateSkill> findByCandidate_Id(Long candidateId);

    boolean existsByCandidate_IdAndSkill_Id(Long candidateId, Long skillId);
}