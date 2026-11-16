package com.hireflow.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminSettingsController {

    @GetMapping("/settings")
    public String adminSettings() {
        return "admin/settings";
    }
}