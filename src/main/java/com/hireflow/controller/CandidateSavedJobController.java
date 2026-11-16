package com.hireflow.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hireflow.entity.SavedJob;
import com.hireflow.entity.User;
import com.hireflow.service.SavedJobService;
import com.hireflow.service.UserService;

@Controller
@RequestMapping("/candidate")
public class CandidateSavedJobController {

    private final UserService userService;
    private final SavedJobService savedJobService;

    public CandidateSavedJobController(
            UserService userService,
            SavedJobService savedJobService) {
        this.userService = userService;
        this.savedJobService = savedJobService;
    }

    @GetMapping("/saved-jobs")
    public String viewSavedJobs(Authentication authentication, Model model) {

        User candidate = userService
                .getUserByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalStateException("Candidate not found"));

        List<SavedJob> savedJobs =
                savedJobService.getSavedJobsByCandidate(candidate.getId());

        model.addAttribute("savedJobs", savedJobs);

        return "candidate/saved-jobs";
    }
}