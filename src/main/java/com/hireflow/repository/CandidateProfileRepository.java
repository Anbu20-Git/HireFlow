package com.hireflow.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hireflow.entity.CandidateProfile;

public interface CandidateProfileRepository
        extends JpaRepository<CandidateProfile, Long> {

    Optional<CandidateProfile> findByUser_Id(Long userId);

    boolean existsByUser_Id(Long userId);
}