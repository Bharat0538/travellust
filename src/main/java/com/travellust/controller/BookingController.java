package com.travellust.controller;

import com.travellust.dto.BookingDto;
import com.travellust.model.Booking;
import com.travellust.model.BookingStatus;
import com.travellust.model.PaymentStatus;
import com.travellust.model.User;
import com.travellust.service.BookingService;
import com.travellust.service.TourPackageService;
import com.travellust.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for handling booking-related operations including the booking form,
 * payment processing, and confirmation.
 */
@Controller
@RequestMapping("/bookings")
public class BookingController {
    
    private final BookingService bookingService;
    private final TourPackageService tourPackageService;
    private final UserService userService;
    
    @Autowired
    public BookingController(BookingService bookingService, TourPackageService tourPackageService, UserService userService) {
        this.bookingService = bookingService;
        this.tourPackageService = tourPackageService;
        this.userService = userService;
    }
    
    @GetMapping
    public String listBookings(
            Model model, 
            Authentication authentication,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String fromDate) {
        
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            List<BookingDto> bookings;
            
            // Apply filters if provided
            if (status != null && !status.isEmpty()) {
                try {
                    BookingStatus bookingStatus = BookingStatus.valueOf(status);
                    bookings = bookingService.getBookingsByUserAndStatus(
                            userService.findByEmail(username).getId(), 
                            bookingStatus);
                    model.addAttribute("filterStatus", bookingStatus);
                } catch (IllegalArgumentException e) {
                    // Invalid enum value, show all bookings
                    bookings = bookingService.getBookingsByUsername(username);
                }
            } else if (fromDate != null && !fromDate.isEmpty()) {
                LocalDate date = LocalDate.parse(fromDate);
                // Assuming we want bookings from this date onwards
                bookings = bookingService.getBookingsByUsername(username).stream()
                        .filter(b -> b.getTravelDate().isEqual(date) || b.getTravelDate().isAfter(date))
                        .collect(Collectors.toList());
                model.addAttribute("filterFromDate", fromDate);
            } else {
                bookings = bookingService.getBookingsByUsername(username);
            }
            
            model.addAttribute("bookings", bookings);
        }
        return "bookings/list";
    }
    
    @PostMapping("/create")
    public String createBooking(
            @RequestParam("packageId") Long packageId,
            @RequestParam("packageName") String packageName,
            @RequestParam("destinationName") String destinationName,
            @RequestParam("packagePrice") Double packagePrice,
            @RequestParam("travelDate") String travelDate,
            @RequestParam("numberOfPeople") Integer numberOfPeople,
            @RequestParam("contactName") String contactName,
            @RequestParam("contactEmail") String contactEmail,
            @RequestParam("contactPhone") String contactPhone,
            @RequestParam(value = "specialRequests", required = false) String specialRequests,
            RedirectAttributes redirectAttributes) {
        
        try {
            // Create booking object
            BookingDto bookingDto = new BookingDto();
            bookingDto.setPackageId(packageId);
            bookingDto.setPackageName(packageName);
            bookingDto.setDestinationName(destinationName);
            bookingDto.setTravelDate(LocalDate.parse(travelDate));
            bookingDto.setNumberOfPeople(numberOfPeople);
            bookingDto.setPackagePrice(BigDecimal.valueOf(packagePrice));
            bookingDto.setTotalAmount(BigDecimal.valueOf(packagePrice * numberOfPeople));
            bookingDto.setTotalPrice(packagePrice * numberOfPeople);
            bookingDto.setContactName(contactName);
            bookingDto.setContactEmail(contactEmail);
            bookingDto.setContactPhone(contactPhone);
            bookingDto.setSpecialRequests(specialRequests);
            bookingDto.setBookingDate(LocalDate.now());
            bookingDto.setPaymentStatus(PaymentStatus.PENDING);
            bookingDto.setStatus(BookingStatus.PENDING);
            
            // Get current user if authenticated
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated() && 
                    !"anonymousUser".equals(authentication.getPrincipal())) {
                String username = authentication.getName();
                // Get the user ID from the username/email
                User user = userService.findByEmail(username);
                bookingDto.setUserId(user.getId());
            } else {
                // For demo/testing: use a default user if not authenticated
                try {
                    // Try to get a default user (first in the database)
                    User defaultUser = userService.getDefaultUser();
                    bookingDto.setUserId(defaultUser.getId());
                } catch (Exception e) {
                    throw new RuntimeException("Please log in to book a tour package or create a test user.");
                }
            }
            
            // Save booking
            BookingDto savedBooking = bookingService.createBooking(bookingDto);
            
            // Redirect to payment page or confirmation
            redirectAttributes.addFlashAttribute("success", 
                    "Your booking has been created successfully! Booking reference: " + savedBooking.getId());
            return "redirect:/bookings/confirmation/" + savedBooking.getId();
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", 
                    "There was an error processing your booking: " + e.getMessage());
            return "redirect:/tour-packages/details/" + packageId;
        }
    }
    
    @GetMapping("/confirmation/{id}")
    public String confirmationPage(@PathVariable("id") Long id, Model model) {
        BookingDto booking = bookingService.getBookingById(id);
        model.addAttribute("booking", booking);
        return "bookings/confirmation";
    }
    
    @GetMapping("/details/{id}")
    public String bookingDetails(@PathVariable("id") Long id, Model model) {
        BookingDto booking = bookingService.getBookingById(id);
        model.addAttribute("booking", booking);
        return "bookings/details";
    }
    
    @GetMapping("/{id}")
    public String viewBooking(@PathVariable("id") Long id, Model model) {
        // Reuse the same logic as bookingDetails
        BookingDto booking = bookingService.getBookingById(id);
        model.addAttribute("booking", booking);
        return "bookings/details";
    }
    
    @PostMapping("/{id}/cancel")
    public String cancelBooking(
            @PathVariable("id") Long id,
            RedirectAttributes redirectAttributes,
            Authentication authentication) {
        
        try {
            // Check if the booking belongs to the current user
            if (authentication != null && authentication.isAuthenticated()) {
                String username = authentication.getName();
                BookingDto booking = bookingService.getBookingById(id);
                User user = userService.findByEmail(username);
                
                if (!booking.getUserId().equals(user.getId())) {
                    redirectAttributes.addFlashAttribute("error", "You are not authorized to cancel this booking.");
                    return "redirect:/bookings";
                }
                
                // Only allow cancellation of PENDING bookings
                if (booking.getStatus() != BookingStatus.PENDING) {
                    redirectAttributes.addFlashAttribute("error", 
                            "Only pending bookings can be cancelled.");
                    return "redirect:/bookings";
                }
                
                // Update the booking status to CANCELLED
                bookingService.updateBookingStatus(id, "CANCELLED");
                
                redirectAttributes.addFlashAttribute("success", 
                        "Booking " + booking.getBookingReference() + " has been cancelled successfully.");
            } else {
                redirectAttributes.addFlashAttribute("error", "You must be logged in to cancel a booking.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", 
                    "There was an error cancelling your booking: " + e.getMessage());
        }
        
        return "redirect:/bookings";
    }
} 