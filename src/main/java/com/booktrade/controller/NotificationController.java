package com.booktrade.controller;

import com.booktrade.config.LoginInterceptor;
import com.booktrade.entity.Notification;
import com.booktrade.entity.User;
import com.booktrade.service.NotificationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/notifications")
    public String list(HttpSession session, Model model) {
        User user = (User) session.getAttribute(LoginInterceptor.SESSION_USER);
        List<Notification> notifications = notificationService.listByUser(user.getId());
        model.addAttribute("notifications", notifications);
        model.addAttribute("loginUser", user);
        return "notifications";
    }

    @GetMapping("/notifications/read/{id}")
    public String markRead(@PathVariable Long id,
                           HttpSession session,
                           RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute(LoginInterceptor.SESSION_USER);
        notificationService.markAsRead(id, user.getId());
        return "redirect:/notifications";
    }

    @GetMapping("/notifications/read-all")
    public String markAllRead(HttpSession session,
                              RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute(LoginInterceptor.SESSION_USER);
        notificationService.markAllAsRead(user.getId());
        redirectAttributes.addFlashAttribute("success", "msg.notification.all_read");
        return "redirect:/notifications";
    }
}
