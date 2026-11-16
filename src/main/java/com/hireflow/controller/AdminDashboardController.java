package com.hireflow.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hireflow.entity.User;
import com.hireflow.entity.Job;
import com.hireflow.entity.Application;
import com.hireflow.entity.Interview;

import com.hireflow.service.UserService;
import com.hireflow.service.CompanyService;
import com.hireflow.service.JobService;
import com.hireflow.service.ApplicationService;
import com.hireflow.service.InterviewService;

@Controller
@RequestMapping("/admin")
public class AdminDashboardController {

    private final UserService userService;
    private final CompanyService companyService;
    private final JobService jobService;
    private final ApplicationService applicationService;
    private final InterviewService interviewService;

    public AdminDashboardController(
            UserService userService,
            CompanyService companyService,
            JobService jobService,
            ApplicationService applicationService,
            InterviewService interviewService) {

        this.userService = userService;
        this.companyService = companyService;
        this.jobService = jobService;
        this.applicationService = applicationService;
        this.interviewService = interviewService;
    }

    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {

        // Users
        List<User> users = userService.getAllUsers();

        long totalUsers = users.size();

        long totalCandidates = users.stream()
                .filter(user -> "CANDIDATE".equalsIgnoreCase(
                        String.valueOf(user.getRole())))
                .count();

        long totalRecruiters = users.stream()
                .filter(user -> "RECRUITER".equalsIgnoreCase(
                        String.valueOf(user.getRole())))
                .count();

        long totalAdmins = users.stream()
                .filter(user -> "ADMIN".equalsIgnoreCase(
                        String.valueOf(user.getRole())))
                .count();

        // Companies
        long totalCompanies = companyService.getAllCompanies().size();

        // Jobs
        List<Job> jobs = jobService.getAllJobs();

        long totalJobs = jobs.size();

        long openJobs = jobs.stream()
                .filter(job -> "OPEN".equalsIgnoreCase(
                        String.valueOf(job.getStatus())))
                .count();

        long closedJobs = jobs.stream()
                .filter(job -> "CLOSED".equalsIgnoreCase(
                        String.valueOf(job.getStatus())))
                .count();

        long draftJobs = jobs.stream()
                .filter(job -> "DRAFT".equalsIgnoreCase(
                        String.valueOf(job.getStatus())))
                .count();

        // Applications
        List<Application> applications =
                applicationService.getAllApplications();

        long totalApplications = applications.size();

        long appliedApplications = applications.stream()
                .filter(application -> "APPLIED".equalsIgnoreCase(
                        String.valueOf(application.getStatus())))
                .count();

        long shortlistedApplications = applications.stream()
                .filter(application -> "SHORTLISTED".equalsIgnoreCase(
                        String.valueOf(application.getStatus())))
                .count();

        long rejectedApplications = applications.stream()
                .filter(application -> "REJECTED".equalsIgnoreCase(
                        String.valueOf(application.getStatus())))
                .count();

        long selectedApplications = applications.stream()
                .filter(application -> "SELECTED".equalsIgnoreCase(
                        String.valueOf(application.getStatus())))
                .count();

        // Interviews
        List<Interview> interviews =
                interviewService.getAllInterviews();

        long totalInterviews = interviews.size();

        long scheduledInterviews = interviews.stream()
                .filter(interview -> "SCHEDULED".equalsIgnoreCase(
                        String.valueOf(interview.getStatus())))
                .count();

        long completedInterviews = interviews.stream()
                .filter(interview -> "COMPLETED".equalsIgnoreCase(
                        String.valueOf(interview.getStatus())))
                .count();

        long cancelledInterviews = interviews.stream()
                .filter(interview -> "CANCELLED".equalsIgnoreCase(
                        String.valueOf(interview.getStatus())))
                .count();

        // Dashboard statistics
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("totalCandidates", totalCandidates);
        model.addAttribute("totalRecruiters", totalRecruiters);
        model.addAttribute("totalAdmins", totalAdmins);

        model.addAttribute("totalCompanies", totalCompanies);

        model.addAttribute("totalJobs", totalJobs);
        model.addAttribute("openJobs", openJobs);
        model.addAttribute("closedJobs", closedJobs);
        model.addAttribute("draftJobs", draftJobs);

        model.addAttribute("totalApplications", totalApplications);
        model.addAttribute("appliedApplications", appliedApplications);
        model.addAttribute("shortlistedApplications", shortlistedApplications);
        model.addAttribute("rejectedApplications", rejectedApplications);
        model.addAttribute("selectedApplications", selectedApplications);

        model.addAttribute("totalInterviews", totalInterviews);
        model.addAttribute("scheduledInterviews", scheduledInterviews);
        model.addAttribute("completedInterviews", completedInterviews);
        model.addAttribute("cancelledInterviews", cancelledInterviews);

        return "admin/dashboard";
    }
}