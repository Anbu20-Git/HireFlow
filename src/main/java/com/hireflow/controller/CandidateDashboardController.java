package com.hireflow.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hireflow.entity.User;
import com.hireflow.entity.Application;
import com.hireflow.entity.Interview;

import com.hireflow.service.UserService;
import com.hireflow.service.ApplicationService;
import com.hireflow.service.SavedJobService;
import com.hireflow.service.InterviewService;

@Controller
@RequestMapping("/candidate")
public class CandidateDashboardController {

    private final UserService userService;
    private final ApplicationService applicationService;
    private final SavedJobService savedJobService;
    private final InterviewService interviewService;

    public CandidateDashboardController(
            UserService userService,
            ApplicationService applicationService,
            SavedJobService savedJobService,
            InterviewService interviewService) {

        this.userService = userService;
        this.applicationService = applicationService;
        this.savedJobService = savedJobService;
        this.interviewService = interviewService;
    }

    @GetMapping("/dashboard")
    public String candidateDashboard(
            Authentication authentication, Model model) {

        User user = userService.getUserByEmail(authentication.getName())
                .orElseThrow(() ->
                    new IllegalStateException("User not found"));

        model.addAttribute("user", user);

        // Get candidate applications
        List<Application> applications =
                applicationService.getApplicationsByCandidate(user.getId());

        // Total Applications
        model.addAttribute("totalApplications", applications.size());

        // Shortlisted Applications
        long shortlisted = applications.stream()
                .filter(application -> application.getStatus()
                        == Application.ApplicationStatus.SHORTLISTED)
                .count();

        model.addAttribute("shortlisted", shortlisted);

        // Saved Jobs
        int savedJobs = savedJobService
                .getSavedJobsByCandidate(user.getId()).size();

        model.addAttribute("savedJobs", savedJobs);

        // Upcoming Interviews
        LocalDateTime now = LocalDateTime.now();

        long upcomingInterviews = applications.stream()
                .flatMap(application ->
                        interviewService
                                .getInterviewsByApplication(application.getId())
                                .stream())
                .filter(interview ->
                        interview.getStatus() == Interview.InterviewStatus.SCHEDULED
                        && interview.getInterviewDate() != null
                        && interview.getInterviewDate().isAfter(now))
                .count();

        model.addAttribute("upcomingInterviews", upcomingInterviews);

        return "candidate/dashboard";
    }
}