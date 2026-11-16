package com.hireflow.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hireflow.entity.Company;
import com.hireflow.entity.Job;
import com.hireflow.entity.User;
import com.hireflow.service.CompanyService;
import com.hireflow.service.JobService;
import com.hireflow.service.UserService;

@Controller
@RequestMapping("/recruiter/company")
public class CompanyController {

    private final CompanyService companyService;
    private final JobService jobService;
    private final UserService userService;

    public CompanyController(
            CompanyService companyService,
            JobService jobService,
            UserService userService) {

        this.companyService = companyService;
        this.jobService = jobService;
        this.userService = userService;
    }

    // View Company Profile

    @GetMapping
    public String viewCompanyProfile(
            Authentication authentication,
            Model model) {

        User recruiter = userService
                .getUserByEmail(authentication.getName())
                .orElseThrow(() ->
                        new IllegalStateException("Recruiter not found"));

        List<Job> jobs =
                jobService.getJobsByRecruiter(recruiter.getId());

        List<Company> companies = new ArrayList<>();

        for (Job job : jobs) {

            Company company = job.getCompany();

            if (company != null &&
                    companies.stream().noneMatch(
                            c -> c.getId().equals(company.getId()))) {

                companies.add(company);
            }
        }

        model.addAttribute("companies", companies);

        model.addAttribute(
                "company",
                companies.isEmpty() ? new Company() : companies.get(0));

        return "recruiter/company-profile";
    }

    // Update Company Profile

    @PostMapping("/update")
    public String updateCompanyProfile(
            @ModelAttribute Company company,
            Authentication authentication) {

        User recruiter = userService
                .getUserByEmail(authentication.getName())
                .orElseThrow(() ->
                        new IllegalStateException("Recruiter not found"));

        List<Job> jobs =
                jobService.getJobsByRecruiter(recruiter.getId());

        boolean ownsCompany = jobs.stream()
                .anyMatch(job ->
                        job.getCompany() != null
                        && job.getCompany().getId().equals(company.getId()));

        if (!ownsCompany) {
            throw new IllegalStateException(
                    "You cannot update another company's profile");
        }

        Company existingCompany = companyService
                .getCompanyById(company.getId())
                .orElseThrow(() ->
                        new IllegalStateException("Company not found"));

        existingCompany.setCompanyName(company.getCompanyName());
        existingCompany.setIndustry(company.getIndustry());
        existingCompany.setWebsite(company.getWebsite());
        existingCompany.setLocation(company.getLocation());
        existingCompany.setDescription(company.getDescription());

        companyService.saveCompany(existingCompany);

        return "redirect:/recruiter/company";
    }
}