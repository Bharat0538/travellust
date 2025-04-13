package com.travellust.controller;

import com.travellust.dto.PaymentDto;
import com.travellust.model.PaymentMethod;
import com.travellust.model.PaymentStatus;
import com.travellust.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {
    
    private final PaymentService paymentService;
    
    @GetMapping
    public String getUserPayments(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = getUserIdFromAuthentication(auth);
        
        List<PaymentDto> payments = paymentService.getPaymentsByUserId(userId);
        model.addAttribute("payments", payments);
        return "payments/list";
    }
    
    @GetMapping("/{id}")
    public String getPaymentDetails(@PathVariable Long id, Model model) {
        PaymentDto payment = paymentService.getPaymentById(id);
        model.addAttribute("payment", payment);
        return "payments/details";
    }
    
    @GetMapping("/transaction/{transactionId}")
    public String getPaymentByTransactionId(@PathVariable String transactionId, Model model) {
        PaymentDto payment = paymentService.getPaymentByTransactionId(transactionId);
        model.addAttribute("payment", payment);
        return "payments/details";
    }
    
    @GetMapping("/booking/{bookingId}")
    public String getPaymentsByBooking(@PathVariable Long bookingId, Model model) {
        List<PaymentDto> payments = paymentService.getPaymentsByBookingId(bookingId);
        model.addAttribute("payments", payments);
        model.addAttribute("bookingId", bookingId);
        return "payments/list";
    }
    
    // Admin-only endpoints for managing all payments
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public String getAllPayments(
            @RequestParam(required = false) PaymentStatus status,
            @RequestParam(required = false) PaymentMethod method,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            Model model) {
        
        List<PaymentDto> payments;
        
        if (status != null) {
            payments = paymentService.getPaymentsByStatus(status);
            model.addAttribute("filterStatus", status);
        } else if (method != null) {
            payments = paymentService.getPaymentsByMethod(method);
            model.addAttribute("filterMethod", method);
        } else if (startDate != null && endDate != null) {
            payments = paymentService.getPaymentsByDateRange(startDate, endDate);
            model.addAttribute("filterStartDate", startDate);
            model.addAttribute("filterEndDate", endDate);
        } else {
            payments = paymentService.getAllPayments();
        }
        
        model.addAttribute("payments", payments);
        model.addAttribute("statuses", PaymentStatus.values());
        model.addAttribute("methods", PaymentMethod.values());
        return "admin/payments/list";
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/{id}/status")
    public String updatePaymentStatus(
            @PathVariable Long id,
            @RequestParam PaymentStatus status,
            RedirectAttributes redirectAttributes) {
        
        paymentService.updatePaymentStatus(id, status);
        redirectAttributes.addFlashAttribute("success", "Payment status updated successfully!");
        return "redirect:/payments/admin";
    }
    
    // Helper method to get user ID from Authentication
    private Long getUserIdFromAuthentication(Authentication authentication) {
        // This method should be implemented based on your authentication system
        // For example, if you're using a UserDetails implementation:
        // CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        // return userDetails.getId();
        
        // Placeholder implementation - replace with actual implementation
        return 1L; // Dummy user ID
    }
} 