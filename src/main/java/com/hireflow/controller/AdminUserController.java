package com.hireflow.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hireflow.entity.User;
import com.hireflow.service.UserService;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {

    private final UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String manageUsers(Model model) {

        List<User> users = userService.getAllUsers();

        model.addAttribute("users", users);
        model.addAttribute("totalUsers", users.size());

        return "admin/manage-users";
    }

    @PostMapping("/{id}/toggle-status")
    public String toggleUserStatus(
            @PathVariable Long id,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        User user = userService.getUserById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found"));

        // Protect Admin accounts
        if (user.getRole() == User.Role.ADMIN) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Admin accounts cannot be deactivated.");

            return "redirect:/admin/users";
        }

        // Prevent self-deactivation
        if (user.getEmail().equals(authentication.getName())) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "You cannot deactivate your own account.");

            return "redirect:/admin/users";
        }

        user.setActive(!Boolean.TRUE.equals(user.getActive()));

        userService.saveUser(user);

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "User account status updated successfully.");

        return "redirect:/admin/users";
    }
}