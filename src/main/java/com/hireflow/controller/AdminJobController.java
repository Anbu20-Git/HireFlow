package com.hireflow.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hireflow.entity.Job;
import com.hireflow.service.JobService;

@Controller
@RequestMapping("/admin/jobs")
public class AdminJobController {

    private final JobService jobService;

    public AdminJobController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping
    public String manageJobs(Model model) {

        List<Job> jobs = jobService.getAllJobs();

        model.addAttribute("jobs", jobs);
        model.addAttribute("totalJobs", jobs.size());

        return "admin/manage-jobs";
    }
}