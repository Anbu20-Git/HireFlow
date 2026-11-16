package com.hireflow.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hireflow.entity.Notification;
import com.hireflow.entity.User;
import com.hireflow.service.NotificationService;
import com.hireflow.service.UserService;

@Controller
@RequestMapping("/candidate")
public class CandidateNotificationController {

    private final UserService userService;
    private final NotificationService notificationService;

    public CandidateNotificationController(
            UserService userService,
            NotificationService notificationService) {
        this.userService = userService;
        this.notificationService = notificationService;
    }

    @GetMapping("/notifications")
    public String viewNotifications(
            Authentication authentication, Model model) {

        User candidate = userService
                .getUserByEmail(authentication.getName())
                .orElseThrow(() ->
                        new IllegalStateException("Candidate not found"));

        List<Notification> notifications =
                notificationService.getNotificationsByUser(candidate.getId());

        model.addAttribute("notifications", notifications);

        return "candidate/notifications";
    }

    @PostMapping("/notifications/{id}/read")
    public String markAsRead(
            @PathVariable Long id,
            Authentication authentication) {

        User candidate = userService
                .getUserByEmail(authentication.getName())
                .orElseThrow(() ->
                        new IllegalStateException("Candidate not found"));

        Notification notification = notificationService
                .getNotificationsByUser(candidate.getId())
                .stream()
                .filter(n -> n.getId().equals(id))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalStateException("Notification not found"));

        notificationService.markAsRead(id, candidate.getId());

        return "redirect:/candidate/notifications";
    }
}