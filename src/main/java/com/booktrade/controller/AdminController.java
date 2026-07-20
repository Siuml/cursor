package com.booktrade.controller;

import com.booktrade.config.LoginInterceptor;
import com.booktrade.entity.Book;
import com.booktrade.entity.OrderLog;
import com.booktrade.entity.TradeOrder;
import com.booktrade.entity.User;
import com.booktrade.service.BookService;
import com.booktrade.service.NotificationService;
import com.booktrade.service.OrderService;
import com.booktrade.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final BookService bookService;
    private final NotificationService notificationService;
    private final OrderService orderService;
    private final UserService userService;

    public AdminController(BookService bookService, OrderService orderService, UserService userService, NotificationService notificationService) {
        this.bookService = bookService;
        this.orderService = orderService;
        this.userService = userService;
        this.notificationService = notificationService;
    }

    @GetMapping
    public String index(HttpSession session, Model model) {
        User user = (User) session.getAttribute(LoginInterceptor.SESSION_USER);
        if (user.getRole() != 1) {
            return "redirect:/";
        }
        List<Book> books = bookService.listAll();
        List<TradeOrder> orders = orderService.listAll();
        model.addAttribute("books", books);
        model.addAttribute("orders", orders);
        model.addAttribute("loginUser", user);
        return "admin";
    }

    @GetMapping("/orders")
    public String orders(HttpSession session, Model model,
                         @RequestParam(required = false) Integer status,
                         @RequestParam(required = false) String startDate,
                         @RequestParam(required = false) String endDate) {
        User user = (User) session.getAttribute(LoginInterceptor.SESSION_USER);
        if (user.getRole() != 1) {
            return "redirect:/";
        }

        LocalDateTime startTime = null;
        LocalDateTime endTime = null;

        if (startDate != null && !startDate.isEmpty()) {
            startTime = LocalDate.parse(startDate).atStartOfDay();
        }
        if (endDate != null && !endDate.isEmpty()) {
            endTime = LocalDate.parse(endDate).atTime(LocalTime.MAX);
        }

        List<TradeOrder> orders = orderService.listByStatusAndTimeRange(status, startTime, endTime);

        for (TradeOrder order : orders) {
            User buyer = userService.getById(order.getBuyerId());
            User seller = userService.getById(order.getSellerId());
            Book book = bookService.getById(order.getBookId());
            order.setBuyerId(order.getBuyerId());
            order.setSellerId(order.getSellerId());
            order.setBookId(order.getBookId());
        }

        model.addAttribute("orders", orders);
        model.addAttribute("status", status);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("loginUser", user);
        return "admin-orders";
    }

    @GetMapping("/order/detail/{id}")
    public String orderDetail(@PathVariable Long id, HttpSession session, Model model) {
        User user = (User) session.getAttribute(LoginInterceptor.SESSION_USER);
        if (user.getRole() != 1) {
            return "redirect:/";
        }

        TradeOrder order = orderService.getById(id);
        if (order == null) {
            return "redirect:/admin/orders";
        }

        User buyer = userService.getById(order.getBuyerId());
        User seller = userService.getById(order.getSellerId());
        Book book = bookService.getById(order.getBookId());
        List<OrderLog> logs = orderService.getLogsByOrderId(id);

        model.addAttribute("order", order);
        model.addAttribute("buyer", buyer);
        model.addAttribute("seller", seller);
        model.addAttribute("book", book);
        model.addAttribute("logs", logs);
        model.addAttribute("loginUser", user);
        return "admin-order-detail";
    }

    @GetMapping("/order/confirm/{id}")
    public String confirmOrder(@PathVariable Long id, HttpSession session,
                               RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute(LoginInterceptor.SESSION_USER);
        if (user.getRole() != 1) {
            return "redirect:/";
        }

        TradeOrder order = orderService.getById(id);
        if (order != null && order.getStatus() == 0) {
            orderService.updateStatus(id, 1, user.getId(), user.getNickname());
            redirectAttributes.addFlashAttribute("success", "msg.order.confirmed");
        } else {
            redirectAttributes.addFlashAttribute("error", "msg.order.status_error");
        }
        return "redirect:/admin/order/detail/" + id;
    }

    @GetMapping("/order/complete/{id}")
    public String completeOrder(@PathVariable Long id, HttpSession session,
                                RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute(LoginInterceptor.SESSION_USER);
        if (user.getRole() != 1) {
            return "redirect:/";
        }

        TradeOrder order = orderService.getById(id);
        if (order != null && order.getStatus() == 1) {
            orderService.updateStatus(id, 2, user.getId(), user.getNickname());
            bookService.markSold(order.getBookId());
            redirectAttributes.addFlashAttribute("success", "msg.order.completed");
        } else {
            redirectAttributes.addFlashAttribute("error", "msg.order.status_error");
        }
        return "redirect:/admin/order/detail/" + id;
    }

    @GetMapping("/order/cancel/{id}")
    public String cancelOrder(@PathVariable Long id, HttpSession session,
                              RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute(LoginInterceptor.SESSION_USER);
        if (user.getRole() != 1) {
            return "redirect:/";
        }

        TradeOrder order = orderService.getById(id);
        if (order != null && order.getStatus() == 0) {
            orderService.updateStatus(id, 3, user.getId(), user.getNickname());
            redirectAttributes.addFlashAttribute("success", "msg.order.cancelled");
        } else {
            redirectAttributes.addFlashAttribute("error", "msg.order.status_error");
        }
        return "redirect:/admin/order/detail/" + id;
    }

    @GetMapping("/book/delete/{id}")
    public String deleteBook(@PathVariable Long id,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute(LoginInterceptor.SESSION_USER);
        if (user.getRole() != 1) {
            return "redirect:/";
        }
        bookService.delete(id);
        redirectAttributes.addFlashAttribute("success", "msg.book.deleted");
        return "redirect:/admin";
    }
    @PostMapping("/broadcast")
    public String broadcast(@RequestParam String title,
                            @RequestParam String content,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute(LoginInterceptor.SESSION_USER);
        if (user == null || user.getRole() != 1) {
            return "redirect:/";
        }
        notificationService.broadcastToAll(title, content, "system");
        redirectAttributes.addFlashAttribute("success", "msg.broadcast.sent");
        return "redirect:/admin";
    }
}