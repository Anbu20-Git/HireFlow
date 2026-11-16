package com.hireflow.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hireflow.entity.Application;
import com.hireflow.service.ApplicationService;

@Controller
@RequestMapping("/admin/applications")
public class AdminApplicationController {

    private final ApplicationService applicationService;

    public AdminApplicationController(
            ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @GetMapping
    public String manageApplications(Model model) {

        List<Application> applications =
                applicationService.getAllApplications();

        model.addAttribute("applications", applications);
        model.addAttribute("totalApplications", applications.size());

        return "admin/manage-applications";
    }
}