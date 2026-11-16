package com.hireflow.controller;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.Authentication;

import com.hireflow.entity.Application;
import com.hireflow.entity.Job;
import com.hireflow.entity.User;
import com.hireflow.service.ApplicationService;
import com.hireflow.service.JobService;
import com.hireflow.service.UserService;

@Controller
@RequestMapping("/candidate")
public class CandidateJobController {

    private final JobService jobService;
    private final UserService userService;
    private final ApplicationService applicationService;

    public CandidateJobController(
            JobService jobService,
            UserService userService,
            ApplicationService applicationService) {

        this.jobService = jobService;
        this.userService = userService;
        this.applicationService = applicationService;
    }

    @GetMapping("/jobs")
    public String findJobs(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String location,
            Model model) {

        List<Job> jobs = jobService
                .getJobsByStatus(Job.JobStatus.OPEN);

        Stream<Job> jobStream = jobs.stream();

        if (keyword != null && !keyword.isBlank()) {
            String searchKeyword = keyword.trim().toLowerCase();

            jobStream = jobStream.filter(job ->
                    job.getJobTitle() != null
                    && job.getJobTitle().toLowerCase()
                            .contains(searchKeyword));
        }

        if (location != null && !location.isBlank()) {
            String searchLocation = location.trim().toLowerCase();

            jobStream = jobStream.filter(job ->
                    job.getLocation() != null
                    && job.getLocation().toLowerCase()
                            .contains(searchLocation));
        }

        model.addAttribute("jobs", jobStream.toList());
        model.addAttribute("keyword", keyword);
        model.addAttribute("location", location);

        return "candidate/jobs";
    }

    @GetMapping("/jobs/{id}")
    public String viewJobDetails(
            @PathVariable Long id,
            @RequestParam(required = false) String applied,
            @RequestParam(required = false) String error,
            Model model) {

        Job job = jobService.getJobById(id)
                .filter(j -> j.getStatus() == Job.JobStatus.OPEN)
                .orElse(null);

        if (job == null) {
            return "redirect:/candidate/jobs?error=notfound";
        }

        model.addAttribute("job", job);
        model.addAttribute("applied", applied);
        model.addAttribute("error", error);

        return "candidate/job-details";
    }

    @PostMapping("/jobs/{id}/apply")
    public String applyForJob(
            @PathVariable Long id,
            Authentication authentication) {

        User candidate = userService
                .getUserByEmail(authentication.getName())
                .orElseThrow(() ->
                        new IllegalStateException("Candidate not found"));

        Job job = jobService.getJobById(id)
                .filter(j -> j.getStatus() == Job.JobStatus.OPEN)
                .orElse(null);

        if (job == null) {
            return "redirect:/candidate/jobs?error=notfound";
        }

        if (applicationService.hasCandidateApplied(
                job.getId(), candidate.getId())) {

            return "redirect:/candidate/jobs/" + id
                    + "?error=already-applied";
        }

        Application application = new Application();

        application.setJob(job);
        application.setCandidate(candidate);
        application.setStatus(Application.ApplicationStatus.APPLIED);

        applicationService.saveApplication(application);

        return "redirect:/candidate/jobs/" + id + "?applied=true";
    }
}