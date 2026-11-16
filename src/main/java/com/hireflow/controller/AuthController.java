package com.hireflow.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hireflow.dto.RegisterRequest;
import com.hireflow.service.RegistrationService;

@Controller
public class AuthController {

    private final RegistrationService registrationService;

    public AuthController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(
            @ModelAttribute RegisterRequest request,
            RedirectAttributes redirectAttributes) {

        try {
            registrationService.registerCandidate(request);
            redirectAttributes.addFlashAttribute(
                    "success", "Registration successful! Please login.");

            return "redirect:/login";

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/register";
        }
    }
}