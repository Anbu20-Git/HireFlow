package com.hireflow.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hireflow.entity.CandidateProfile;
import com.hireflow.entity.User;
import com.hireflow.service.CandidateProfileService;
import com.hireflow.service.UserService;

@Controller
@RequestMapping("/candidate")
public class CandidateProfileController {

    private final UserService userService;
    private final CandidateProfileService profileService;

    public CandidateProfileController(
            UserService userService,
            CandidateProfileService profileService) {

        this.userService = userService;
        this.profileService = profileService;
    }

    @GetMapping("/profile")
    public String viewProfile(
            Authentication authentication, Model model) {

        User user = userService.getUserByEmail(authentication.getName())
                .orElseThrow(() ->
                    new IllegalStateException("User not found"));

        model.addAttribute("user", user);

        model.addAttribute("profile",
                profileService.getProfileByUserId(user.getId())
                        .orElse(null));

        return "candidate/profile";
    }

    @GetMapping("/profile/edit")
    public String editProfile(
            Authentication authentication, Model model) {

        User user = userService.getUserByEmail(authentication.getName())
                .orElseThrow(() ->
                    new IllegalStateException("User not found"));

        CandidateProfile profile = profileService
                .getProfileByUserId(user.getId())
                .orElseGet(CandidateProfile::new);

        model.addAttribute("user", user);
        model.addAttribute("profileForm", profile);

        return "candidate/edit-profile";
    }

    @PostMapping("/profile/save")
    public String saveProfile(
            @ModelAttribute("profileForm") CandidateProfile form,
            Authentication authentication) {

        User user = userService.getUserByEmail(authentication.getName())
                .orElseThrow(() ->
                    new IllegalStateException("User not found"));

        CandidateProfile profile = profileService
                .getProfileByUserId(user.getId())
                .orElseGet(CandidateProfile::new);

        profile.setUser(user);
        profile.setQualification(form.getQualification());
        profile.setExperienceYears(form.getExperienceYears());
        profile.setSkills(form.getSkills());
        profile.setLocation(form.getLocation());
        profile.setLinkedinUrl(form.getLinkedinUrl());
        profile.setResumeUrl(form.getResumeUrl());
        profile.setSummary(form.getSummary());

        profileService.saveProfile(profile);

        return "redirect:/candidate/profile";
    }
}