package com.hireflow.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hireflow.entity.Application;
import com.hireflow.entity.Interview;
import com.hireflow.entity.User;
import com.hireflow.service.ApplicationService;
import com.hireflow.service.InterviewService;
import com.hireflow.service.JobService;
import com.hireflow.service.UserService;

@Controller
@RequestMapping("/recruiter/interviews")
public class RecruiterInterviewController {

    private final InterviewService interviewService;
    private final ApplicationService applicationService;
    private final UserService userService;
    private final JobService jobService;

    public RecruiterInterviewController(
            InterviewService interviewService,
            ApplicationService applicationService,
            UserService userService,
            JobService jobService) {

        this.interviewService = interviewService;
        this.applicationService = applicationService;
        this.userService = userService;
        this.jobService = jobService;
    }

    // View Interviews

    @GetMapping
    public String viewInterviews(
            Authentication authentication,
            Model model) {

        User recruiter = getRecruiter(authentication);

        List<Application> shortlistedApplications =
                new ArrayList<>();

        List<Interview> interviews = new ArrayList<>();

        var jobs = jobService.getJobsByRecruiter(recruiter.getId());

        for (var job : jobs) {

            List<Application> applications =
                    applicationService.getApplicationsByJob(job.getId());

            for (Application application : applications) {

                if (application.getStatus()
                        == Application.ApplicationStatus.SHORTLISTED) {

                    shortlistedApplications.add(application);
                }

                interviews.addAll(
                        interviewService.getInterviewsByApplication(
                                application.getId()));
            }
        }

        model.addAttribute(
                "shortlistedApplications", shortlistedApplications);

        model.addAttribute("interviews", interviews);

        return "recruiter/interviews";
    }

    // Open Schedule Interview Page

    @GetMapping("/schedule/{id}")
    public String showScheduleForm(
            @PathVariable Long id,
            Authentication authentication,
            Model model) {

        User recruiter = getRecruiter(authentication);

        Application application = getOwnedApplication(id, recruiter);

        if (application.getStatus()
                != Application.ApplicationStatus.SHORTLISTED) {

            throw new IllegalStateException(
                    "Only shortlisted candidates can be scheduled");
        }

        model.addAttribute("app", application);

        return "recruiter/schedule-interview";
    }

    // Save Interview

    @PostMapping("/schedule")
    public String scheduleInterview(
            @RequestParam Long applicationId,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime interviewDate,
            @RequestParam String interviewMode,
            @RequestParam(required = false) String meetingLink,
            @RequestParam String interviewerName,
            Authentication authentication) {

        User recruiter = getRecruiter(authentication);

        Application application =
                getOwnedApplication(applicationId, recruiter);

        if (application.getStatus()
                != Application.ApplicationStatus.SHORTLISTED) {

            throw new IllegalStateException(
                    "Only shortlisted candidates can be scheduled");
        }

        if (interviewDate == null
                || !interviewDate.isAfter(LocalDateTime.now())) {

            throw new IllegalArgumentException(
                    "Interview date must be in the future");
        }

        Interview interview = new Interview();

        interview.setApplication(application);
        interview.setInterviewDate(interviewDate);
        interview.setInterviewMode(interviewMode);
        interview.setMeetingLink(meetingLink);
        interview.setInterviewerName(interviewerName);
        interview.setStatus(
                Interview.InterviewStatus.SCHEDULED);

        interviewService.saveInterview(interview);

        return "redirect:/recruiter/interviews";
    }

    // Update Interview Status and Feedback

    @PostMapping("/{id}/update")
    public String updateInterview(
            @PathVariable Long id,
            @RequestParam Interview.InterviewStatus status,
            @RequestParam(required = false) String feedback,
            Authentication authentication) {

        User recruiter = getRecruiter(authentication);

        Interview interview = interviewService.getInterviewById(id)
                .orElseThrow(() ->
                        new IllegalStateException("Interview not found"));

        getOwnedApplication(
                interview.getApplication().getId(), recruiter);

        interview.setStatus(status);
        interview.setFeedback(feedback);

        interviewService.saveInterview(interview);

        return "redirect:/recruiter/interviews";
    }

    // Verify Recruiter

    private User getRecruiter(Authentication authentication) {

        return userService.getUserByEmail(authentication.getName())
                .orElseThrow(() ->
                        new IllegalStateException("Recruiter not found"));
    }

    // Verify Application Ownership

    private Application getOwnedApplication(
            Long applicationId, User recruiter) {

        Application application =
                applicationService.getApplicationById(applicationId)
                        .orElseThrow(() ->
                                new IllegalStateException(
                                        "Application not found"));

        if (!application.getJob().getRecruiter().getId()
                .equals(recruiter.getId())) {

            throw new IllegalStateException(
                    "You cannot access another recruiter's application");
        }

        return application;
    }
}