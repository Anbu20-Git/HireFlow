package com.hireflow.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication) {

        if (authentication == null) {
            return "redirect:/login";
        }

        if (authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return "redirect:/admin/dashboard";
        }

        if (authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_RECRUITER"))) {
            return "redirect:/recruiter/dashboard";
        }

        if (authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_CANDIDATE"))) {
            return "redirect:/candidate/dashboard";
        }

        return "redirect:/login";
    }
}