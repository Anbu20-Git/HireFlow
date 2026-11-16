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
public class ReportsAnalyticsController {

    private final UserService userService;
    private final CompanyService companyService;
    private final JobService jobService;
    private final ApplicationService applicationService;
    private final InterviewService interviewService;

    public ReportsAnalyticsController(
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

    @GetMapping("/reports")
    public String reports(Model model) {

        List<User> users = userService.getAllUsers();
        List<Job> jobs = jobService.getAllJobs();
        List<Application> applications =
                applicationService.getAllApplications();
        List<Interview> interviews =
                interviewService.getAllInterviews();

        model.addAttribute("totalUsers", users.size());
        model.addAttribute("totalCandidates",
                users.stream().filter(u -> "CANDIDATE".equals(
                        String.valueOf(u.getRole()))).count());
        model.addAttribute("totalRecruiters",
                users.stream().filter(u -> "RECRUITER".equals(
                        String.valueOf(u.getRole()))).count());
        model.addAttribute("totalAdmins",
                users.stream().filter(u -> "ADMIN".equals(
                        String.valueOf(u.getRole()))).count());

        model.addAttribute("totalCompanies",
                companyService.getAllCompanies().size());

        model.addAttribute("totalJobs", jobs.size());
        model.addAttribute("openJobs",
                jobs.stream().filter(j -> "OPEN".equals(
                        String.valueOf(j.getStatus()))).count());
        model.addAttribute("closedJobs",
                jobs.stream().filter(j -> "CLOSED".equals(
                        String.valueOf(j.getStatus()))).count());
        model.addAttribute("draftJobs",
                jobs.stream().filter(j -> "DRAFT".equals(
                        String.valueOf(j.getStatus()))).count());

        model.addAttribute("totalApplications", applications.size());
        model.addAttribute("appliedApplications",
                applications.stream().filter(a -> "APPLIED".equals(
                        String.valueOf(a.getStatus()))).count());
        model.addAttribute("shortlistedApplications",
                applications.stream().filter(a -> "SHORTLISTED".equals(
                        String.valueOf(a.getStatus()))).count());
        model.addAttribute("rejectedApplications",
                applications.stream().filter(a -> "REJECTED".equals(
                        String.valueOf(a.getStatus()))).count());
        model.addAttribute("selectedApplications",
                applications.stream().filter(a -> "SELECTED".equals(
                        String.valueOf(a.getStatus()))).count());

        model.addAttribute("totalInterviews", interviews.size());
        model.addAttribute("scheduledInterviews",
                interviews.stream().filter(i -> "SCHEDULED".equals(
                        String.valueOf(i.getStatus()))).count());
        model.addAttribute("completedInterviews",
                interviews.stream().filter(i -> "COMPLETED".equals(
                        String.valueOf(i.getStatus()))).count());
        model.addAttribute("cancelledInterviews",
                interviews.stream().filter(i -> "CANCELLED".equals(
                        String.valueOf(i.getStatus()))).count());

        return "admin/reports";
    }
}