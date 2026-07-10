package com.booktrade.controller;

import com.booktrade.config.LoginInterceptor;
import com.booktrade.entity.Book;
import com.booktrade.entity.Category;
import com.booktrade.entity.User;
import com.booktrade.service.BookService;
import com.booktrade.service.CategoryService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    private final BookService bookService;
    private final CategoryService categoryService;

    public HomeController(BookService bookService, CategoryService categoryService) {
        this.bookService = bookService;
        this.categoryService = categoryService;
    }

    @GetMapping("/")
    public String index(@RequestParam(required = false) String keyword,
                        @RequestParam(required = false) Long categoryId,
                        HttpSession session,
                        Model model) {
        List<Book> books = new ArrayList<>();
        List<Category> categories = new ArrayList<>();
        
        try {
            books = bookService.listOnSale(keyword, categoryId);
        } catch (Exception e) {
            logger.error("Failed to fetch books from database: {}", e.getMessage());
        }
        
        try {
            categories = categoryService.listAll();
        } catch (Exception e) {
            logger.error("Failed to fetch categories from database: {}", e.getMessage());
        }
        
        User user = (User) session.getAttribute(LoginInterceptor.SESSION_USER);

        model.addAttribute("books", books);
        model.addAttribute("categories", categories);
        model.addAttribute("keyword", keyword);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("loginUser", user);
        return "index";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }
}
