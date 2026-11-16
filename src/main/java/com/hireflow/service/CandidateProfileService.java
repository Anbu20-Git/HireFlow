package com.hireflow.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.hireflow.entity.CandidateProfile;
import com.hireflow.repository.CandidateProfileRepository;

@Service
public class CandidateProfileService {

    private final CandidateProfileRepository profileRepository;

    public CandidateProfileService(
            CandidateProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public Optional<CandidateProfile> getProfileByUserId(Long userId) {
        return profileRepository.findByUser_Id(userId);
    }

    public boolean profileExists(Long userId) {
        return profileRepository.existsByUser_Id(userId);
    }

    public CandidateProfile saveProfile(CandidateProfile profile) {
        return profileRepository.save(profile);
    }

    public void deleteProfile(Long id) {
        profileRepository.deleteById(id);
    }
}