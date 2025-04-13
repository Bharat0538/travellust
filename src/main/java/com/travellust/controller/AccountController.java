package com.travellust.controller;

import com.travellust.model.User;
import com.travellust.service.BookingService;
import com.travellust.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/account")
public class AccountController {

    private final UserService userService;
    private final BookingService bookingService;

    @Autowired
    public AccountController(UserService userService, BookingService bookingService) {
        this.userService = userService;
        this.bookingService = bookingService;
    }

    @GetMapping
    public String showAccountPage(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            User user = userService.findByEmail(username);
            
            model.addAttribute("user", user);
            model.addAttribute("recentBookings", bookingService.getBookingsByUsername(username));
            model.addAttribute("activeTab", "account");
            
            return "account/index";
        }
        
        return "redirect:/login";
    }
} 