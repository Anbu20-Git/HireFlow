package com.hireflow.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hireflow.entity.Job;
import com.hireflow.entity.User;
import com.hireflow.service.ApplicationService;
import com.hireflow.service.InterviewService;
import com.hireflow.service.JobService;
import com.hireflow.service.UserService;

@Controller
@RequestMapping("/recruiter")
public class RecruiterDashboardController {

    private final JobService jobService;
    private final UserService userService;
    private final ApplicationService applicationService;
    private final InterviewService interviewService;

    public RecruiterDashboardController(
            JobService jobService,
            UserService userService,
            ApplicationService applicationService,
            InterviewService interviewService) {

        this.jobService = jobService;
        this.userService = userService;
        this.applicationService = applicationService;
        this.interviewService = interviewService;
    }

    @GetMapping("/dashboard")
    public String recruiterDashboard(
            Authentication authentication,
            Model model) {

        User recruiter = userService
                .getUserByEmail(authentication.getName())
                .orElseThrow(() ->
                        new IllegalStateException("Recruiter not found"));

        List<Job> jobs =
                jobService.getJobsByRecruiter(recruiter.getId());
        
        model.addAttribute("recentJobs", jobs);

        long activeJobPosts = jobs.stream()
                .filter(job -> "OPEN".equalsIgnoreCase(
                        String.valueOf(job.getStatus())))
                .count();

        long totalApplicants =
                applicationService.getTotalApplicantsByRecruiter(
                        recruiter.getId());

        long shortlisted =
                applicationService.getShortlistedCountByRecruiter(
                        recruiter.getId());

        long upcomingInterviews =
                interviewService.getUpcomingInterviewsByRecruiter(
                        recruiter.getId());

        model.addAttribute("activeJobPosts", activeJobPosts);
        model.addAttribute("totalApplicants", totalApplicants);
        model.addAttribute("shortlisted", shortlisted);
        model.addAttribute("upcomingInterviews", upcomingInterviews);

        return "recruiter/dashboard";
    }
}