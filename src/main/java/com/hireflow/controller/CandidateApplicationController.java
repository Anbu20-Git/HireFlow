package com.hireflow.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hireflow.entity.Application;
import com.hireflow.entity.User;
import com.hireflow.service.ApplicationService;
import com.hireflow.service.UserService;

@Controller
@RequestMapping("/candidate")
public class CandidateApplicationController {

    private final UserService userService;
    private final ApplicationService applicationService;

    public CandidateApplicationController(
            UserService userService,
            ApplicationService applicationService) {

        this.userService = userService;
        this.applicationService = applicationService;
    }

    @GetMapping("/applications")
    public String viewApplications(
            Authentication authentication,
            Model model) {

        User candidate = userService
                .getUserByEmail(authentication.getName())
                .orElseThrow(() ->
                        new IllegalStateException("Candidate not found"));

        List<Application> applications =
                applicationService.getApplicationsByCandidate(
                        candidate.getId());

        model.addAttribute("applications", applications);

        return "candidate/applications";
    }
}