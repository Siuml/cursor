package com.booktrade.config;

import com.booktrade.entity.User;
import com.booktrade.service.NotificationService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAdvice {

    private static final Logger log = LoggerFactory.getLogger(GlobalModelAdvice.class);

    private final NotificationService notificationService;

    public GlobalModelAdvice(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @ModelAttribute("unreadCount")
    public long addUnreadCount(HttpSession session) {
        User user = (User) session.getAttribute(LoginInterceptor.SESSION_USER);
        if (user == null) {
            return 0;
        }
        try {
            return notificationService.countUnread(user.getId());
        } catch (Exception e) {
            log.warn("Failed to count unread notifications for user {}: {}", user.getId(), e.getMessage());
            return 0;
        }
    }
}
