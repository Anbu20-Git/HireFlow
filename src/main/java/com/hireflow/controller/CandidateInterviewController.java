package com.hireflow.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hireflow.entity.Application;
import com.hireflow.entity.Interview;
import com.hireflow.entity.User;
import com.hireflow.repository.InterviewRepository;
import com.hireflow.service.ApplicationService;
import com.hireflow.service.UserService;

@Controller
@RequestMapping("/candidate")
public class CandidateInterviewController {

    private final UserService userService;
    private final ApplicationService applicationService;
    private final InterviewRepository interviewRepository;

    public CandidateInterviewController(
            UserService userService,
            ApplicationService applicationService,
            InterviewRepository interviewRepository) {

        this.userService = userService;
        this.applicationService = applicationService;
        this.interviewRepository = interviewRepository;
    }

    @GetMapping("/interviews")
    public String viewInterviews(
            Authentication authentication,
            Model model) {

        User candidate = userService
                .getUserByEmail(authentication.getName())
                .orElseThrow(() ->
                        new IllegalStateException("Candidate not found"));

        List<Application> applications =
                applicationService.getApplicationsByCandidate(candidate.getId());

        List<Interview> interviews = new ArrayList<>();

        for (Application app : applications) {
            interviews.addAll(
                    interviewRepository.findByApplication_Id(app.getId()));
        }

        model.addAttribute("interviews", interviews);

        return "candidate/interviews";
    }
}