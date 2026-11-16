package com.hireflow.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.hireflow.entity.Interview;
import com.hireflow.repository.InterviewRepository;

@Service
public class InterviewService {

    private final InterviewRepository interviewRepository;

    public InterviewService(InterviewRepository interviewRepository) {
        this.interviewRepository = interviewRepository;
    }

    public List<Interview> getAllInterviews() {
        return interviewRepository.findAll();
    }

    public Optional<Interview> getInterviewById(Long id) {
        return interviewRepository.findById(id);
    }

    public List<Interview> getInterviewsByApplication(Long applicationId) {
        return interviewRepository.findByApplication_Id(applicationId);
    }

    public List<Interview> getInterviewsByStatus(
            Interview.InterviewStatus status) {
        return interviewRepository.findByStatus(status);
    }

    public long getUpcomingInterviewsByRecruiter(Long recruiterId) {
        return interviewRepository
                .countByApplication_Job_Recruiter_IdAndStatusAndInterviewDateAfter(
                        recruiterId,
                        Interview.InterviewStatus.SCHEDULED,
                        LocalDateTime.now());
    }

    public Interview saveInterview(Interview interview) {
        return interviewRepository.save(interview);
    }

    public void deleteInterview(Long id) {
        interviewRepository.deleteById(id);
    }
}