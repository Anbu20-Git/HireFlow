package com.hireflow.controller;

import java.util.List;
import java.util.ArrayList;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hireflow.entity.Job;
import com.hireflow.entity.User;
import com.hireflow.service.JobService;
import com.hireflow.service.UserService;
import com.hireflow.service.CompanyService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import com.hireflow.entity.Company;

import org.springframework.web.bind.annotation.PathVariable;

import com.hireflow.service.ApplicationService;
import com.hireflow.entity.Application;

@Controller
@RequestMapping("/recruiter")
public class RecruiterJobController {

    private final JobService jobService;
    private final UserService userService;
    private final CompanyService companyService;
    private final ApplicationService applicationService;

    public RecruiterJobController(
            JobService jobService,
            UserService userService,
            CompanyService companyService,
            ApplicationService applicationService ) {
    	
        this.jobService = jobService;
        this.userService = userService;
        this.companyService = companyService;
        this.applicationService = applicationService;
    }

    @GetMapping("/jobs")
    public String viewMyJobs(
            Authentication authentication,
            Model model) {

        User recruiter = userService
                .getUserByEmail(authentication.getName())
                .orElseThrow(() ->
                        new IllegalStateException("Recruiter not found"));

        List<Job> jobs =
                jobService.getJobsByRecruiter(recruiter.getId());

        model.addAttribute("jobs", jobs);

        return "recruiter/jobs";
    }
    
    @GetMapping("/jobs/new")
    public String showCreateJobForm(Model model) {

        model.addAttribute("job", new Job());

        model.addAttribute("companies",
                companyService.getAllCompanies());

        return "recruiter/create-job";
    }
    
    @PostMapping("/jobs/create")
    public String createJob(
            @ModelAttribute("job") Job job,
            @RequestParam Long companyId,
            Authentication authentication) {

        User recruiter = userService
                .getUserByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalStateException("Recruiter not found"));

        Company company = companyService.getCompanyById(companyId)
                .orElseThrow(() -> new IllegalStateException("Company not found"));

        job.setRecruiter(recruiter);
        job.setCompany(company);
        job.setStatus(Job.JobStatus.OPEN);

        jobService.saveJob(job);

        return "redirect:/recruiter/jobs";
    }
    
    @GetMapping("/jobs/edit/{id}")
    public String showEditJobForm(
            @org.springframework.web.bind.annotation.PathVariable Long id,
            Authentication authentication,
            Model model) {

        User recruiter = userService
                .getUserByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalStateException("Recruiter not found"));

        Job job = jobService.getJobById(id)
                .orElseThrow(() -> new IllegalStateException("Job not found"));

        if (!job.getRecruiter().getId().equals(recruiter.getId())) {
            throw new IllegalStateException("You cannot edit another recruiter's job");
        }

        model.addAttribute("job", job);
        model.addAttribute("companies", companyService.getAllCompanies());

        return "recruiter/edit-job";
    }
    
    @PostMapping("/jobs/update")
    public String updateJob(
            @ModelAttribute("job") Job updatedJob,
            @RequestParam Long companyId,
            Authentication authentication) {

        User recruiter = userService
                .getUserByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalStateException("Recruiter not found"));

        Job existingJob = jobService.getJobById(updatedJob.getId())
                .orElseThrow(() -> new IllegalStateException("Job not found"));

        if (!existingJob.getRecruiter().getId().equals(recruiter.getId())) {
            throw new IllegalStateException("You cannot edit another recruiter's job");
        }

        Company company = companyService.getCompanyById(companyId)
                .orElseThrow(() -> new IllegalStateException("Company not found"));

        existingJob.setJobTitle(updatedJob.getJobTitle());
        existingJob.setCompany(company);
        existingJob.setLocation(updatedJob.getLocation());
        existingJob.setJobType(updatedJob.getJobType());
        existingJob.setExperienceRequired(updatedJob.getExperienceRequired());
        existingJob.setSalaryMin(updatedJob.getSalaryMin());
        existingJob.setSalaryMax(updatedJob.getSalaryMax());
        existingJob.setRequiredSkills(updatedJob.getRequiredSkills());
        existingJob.setDescription(updatedJob.getDescription());

        jobService.saveJob(existingJob);

        return "redirect:/recruiter/jobs";
    }
    
    @PostMapping("/jobs/close/{id}")
    public String closeJob(
            @PathVariable Long id,
            Authentication authentication) {

        User recruiter = userService
                .getUserByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalStateException("Recruiter not found"));

        Job job = jobService.getJobById(id)
                .orElseThrow(() -> new IllegalStateException("Job not found"));

        if (!job.getRecruiter().getId().equals(recruiter.getId())) {
            throw new IllegalStateException("You cannot close another recruiter's job");
        }

        job.setStatus(Job.JobStatus.CLOSED);

        jobService.saveJob(job);

        return "redirect:/recruiter/jobs";
    }
    
    @GetMapping("/jobs/{jobId}/applicants")
    public String viewApplicants(
            @PathVariable Long jobId,
            Authentication authentication,
            Model model) {

        User recruiter = userService
                .getUserByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalStateException("Recruiter not found"));

        Job job = jobService.getJobById(jobId)
                .orElseThrow(() -> new IllegalStateException("Job not found"));

        if (!job.getRecruiter().getId().equals(recruiter.getId())) {
            throw new IllegalStateException("You cannot view another recruiter's applicants");
        }

        List<Application> applications =
                applicationService.getApplicationsByJob(jobId);

        model.addAttribute("job", job);
        model.addAttribute("applications", applications);

        return "recruiter/applicants";
    }
    
    @GetMapping("/applicants")
    public String viewAllApplicants(Authentication authentication, Model model) {

        User recruiter = userService.getUserByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalStateException("Recruiter not found"));

        List<Job> jobs = jobService.getJobsByRecruiter(recruiter.getId());

        List<Application> applications = new ArrayList<>();

        for (Job job : jobs) {
            applications.addAll(
                    applicationService.getApplicationsByJob(job.getId())
            );
        }

        model.addAttribute("applications", applications);

        return "recruiter/applicants";
    }
    
    @PostMapping("/applications/{applicationId}/status")
    public String updateApplicationStatus(
            @PathVariable Long applicationId,
            @RequestParam Application.ApplicationStatus status,
            Authentication authentication) {

        User recruiter = userService.getUserByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalStateException("Recruiter not found"));

        Application application = applicationService.getApplicationById(applicationId)
                .orElseThrow(() -> new IllegalStateException("Application not found"));

        if (!application.getJob().getRecruiter().getId()
                .equals(recruiter.getId())) {
            throw new IllegalStateException(
                    "You cannot update another recruiter's application");
        }

        application.setStatus(status);
        applicationService.saveApplication(application);

        return "redirect:/recruiter/applicants";
    }
}