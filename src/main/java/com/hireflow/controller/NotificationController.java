package com.hireflow.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hireflow.entity.Notification;
import com.hireflow.entity.User;
import com.hireflow.service.NotificationService;
import com.hireflow.service.UserService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/recruiter/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final UserService userService;

    public NotificationController(
            NotificationService notificationService,
            UserService userService) {

        this.notificationService = notificationService;
        this.userService = userService;
    }

    // View Recruiter Notifications

    @GetMapping
    public String viewNotifications(
            Authentication authentication,
            Model model) {

        User recruiter = userService
                .getUserByEmail(authentication.getName())
                .orElseThrow(() ->
                        new IllegalStateException("Recruiter not found"));

        List<Notification> notifications =
                notificationService.getNotificationsByUser(recruiter.getId());

        List<Notification> unreadNotifications =
                notificationService.getUnreadNotifications(recruiter.getId());

        model.addAttribute("notifications", notifications);
        model.addAttribute("unreadCount", unreadNotifications.size());

        return "recruiter/notifications";
    }
    
    @PostMapping("/read/{id}")
    public String markAsRead(
            @PathVariable Long id,
            Authentication authentication) {

        User recruiter = userService
                .getUserByEmail(authentication.getName())
                .orElseThrow(() ->
                        new IllegalStateException("Recruiter not found"));

        notificationService.markAsRead(id, recruiter.getId());

        return "redirect:/recruiter/notifications";
    }
}