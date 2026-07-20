package com.booktrade.controller;

import com.booktrade.config.LoginInterceptor;
import com.booktrade.entity.Book;
import com.booktrade.entity.TradeOrder;
import com.booktrade.entity.User;
import com.booktrade.service.BookService;
import com.booktrade.service.OrderService;
import com.booktrade.service.PaymentService;
import com.booktrade.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;
    private final BookService bookService;
    private final PaymentService paymentService;
    private final UserService userService;

    public OrderController(OrderService orderService, BookService bookService, PaymentService paymentService, UserService userService) {
        this.orderService = orderService;
        this.bookService = bookService;
        this.paymentService = paymentService;
        this.userService = userService;
    }

    @PostMapping("/create/{bookId}")
    public String create(@PathVariable Long bookId,
                         HttpSession session,
                         RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute(LoginInterceptor.SESSION_USER);
        Book book = bookService.getById(bookId);

        if (book == null || book.getStatus() != 1) {
            redirectAttributes.addFlashAttribute("error", "msg.book.cannot_buy");
            return "redirect:/";
        }
        if (book.getSellerId().equals(user.getId())) {
            redirectAttributes.addFlashAttribute("error", "msg.book.cannot_buy_own");
            return "redirect:/book/detail/" + bookId;
        }

        TradeOrder order = new TradeOrder();
        order.setBookId(bookId);
        order.setBuyerId(user.getId());
        order.setSellerId(book.getSellerId());
        order.setPrice(book.getPrice());

        orderService.create(order);
        redirectAttributes.addFlashAttribute("success", "msg.order.created");
        return "redirect:/order/my";
    }

    @GetMapping("/my")
    public String myOrders(HttpSession session, Model model) {
        User user = (User) session.getAttribute(LoginInterceptor.SESSION_USER);
        List<TradeOrder> buyOrders = orderService.listByBuyer(user.getId());
        List<TradeOrder> sellOrders = orderService.listBySeller(user.getId());

        model.addAttribute("buyOrders", buyOrders);
        model.addAttribute("sellOrders", sellOrders);
        model.addAttribute("loginUser", user);
        return "my-orders";
    }

    @GetMapping("/confirm/{id}")
    public String confirm(@PathVariable Long id,
                          HttpSession session,
                          RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute(LoginInterceptor.SESSION_USER);
        TradeOrder order = orderService.getById(id);
        if (order != null && order.getSellerId().equals(user.getId()) && order.getStatus() == 0) {
            orderService.updateStatus(id, 1);
            redirectAttributes.addFlashAttribute("success", "msg.order.confirmed");
        }
        return "redirect:/order/my";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id,
                         HttpSession session,
                         Model model) {
        User user = (User) session.getAttribute(LoginInterceptor.SESSION_USER);
        TradeOrder order = orderService.getById(id);
        if (order != null) {
            Book book = bookService.getById(order.getBookId());
            User buyer = userService.getById(order.getBuyerId());
            User seller = userService.getById(order.getSellerId());

            model.addAttribute("order", order);
            model.addAttribute("book", book);
            model.addAttribute("buyer", buyer);
            model.addAttribute("seller", seller);
            model.addAttribute("loginUser", user);
            return "trade-detail";
        }
        model.addAttribute("error", "msg.order.not_found");
        return "trade-detail";
    }

    @GetMapping("/payment/{id}")
    public String payment(@PathVariable Long id,
                          HttpSession session,
                          Model model) {
        User user = (User) session.getAttribute(LoginInterceptor.SESSION_USER);
        TradeOrder order = orderService.getById(id);
        if (order != null && order.getBuyerId().equals(user.getId()) && order.getStatus() == 1) {
            Book book = bookService.getById(order.getBookId());
            User buyer = userService.getById(order.getBuyerId());
            User seller = userService.getById(order.getSellerId());

            model.addAttribute("order", order);
            model.addAttribute("book", book);
            model.addAttribute("buyer", buyer);
            model.addAttribute("seller", seller);
            model.addAttribute("loginUser", user);
            return "payment";
        }
        model.addAttribute("error", "msg.order.cannot_pay");
        return "payment";
    }

    @PostMapping("/pay/{id}")
    public String pay(@PathVariable Long id,
                      @RequestParam(defaultValue = "card") String payMethod,
                      HttpSession session,
                      RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute(LoginInterceptor.SESSION_USER);
        TradeOrder order = orderService.getById(id);
        if (order != null && order.getBuyerId().equals(user.getId()) && order.getStatus() == 1) {
            boolean success = paymentService.pay(id, order.getPrice(), payMethod);
            if (success) {
                redirectAttributes.addFlashAttribute("success", "msg.payment.success");
            } else {
                redirectAttributes.addFlashAttribute("error", "msg.payment.failed");
            }
        }
        return "redirect:/order/detail/" + id;
    }

    @GetMapping("/complete/{id}")
    public String complete(@PathVariable Long id,
                           HttpSession session,
                           RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute(LoginInterceptor.SESSION_USER);
        TradeOrder order = orderService.getById(id);
        if (order != null && (order.getStatus() == 1 || order.getStatus() == 4)
                && (order.getBuyerId().equals(user.getId()) || order.getSellerId().equals(user.getId()))) {
            orderService.updateStatus(id, 2);
            bookService.markSold(order.getBookId());
            redirectAttributes.addFlashAttribute("success", "msg.trade.completed");
        }
        return "redirect:/order/my";
    }

    @GetMapping("/cancel/{id}")
    public String cancel(@PathVariable Long id,
                         HttpSession session,
                         RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute(LoginInterceptor.SESSION_USER);
        TradeOrder order = orderService.getById(id);
        if (order != null && order.getStatus() == 0
                && (order.getBuyerId().equals(user.getId()) || order.getSellerId().equals(user.getId()))) {
            orderService.updateStatus(id, 3);
            redirectAttributes.addFlashAttribute("success", "msg.order.cancelled");
        }
        return "redirect:/order/my";
    }
}