package com.hireflow.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hireflow.entity.Interview;
import com.hireflow.service.InterviewService;

@Controller
@RequestMapping("/admin/interviews")
public class AdminInterviewController {

    private final InterviewService interviewService;

    public AdminInterviewController(InterviewService interviewService) {
        this.interviewService = interviewService;
    }

    @GetMapping
    public String manageInterviews(Model model) {

        List<Interview> interviews =
                interviewService.getAllInterviews();

        model.addAttribute("interviews", interviews);
        model.addAttribute("totalInterviews", interviews.size());

        return "admin/manage-interviews";
    }
}