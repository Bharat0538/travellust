package com.travellust.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "TravelLust - Your Dream Destinations");
        model.addAttribute("activeTab", "home");
        return "index";
    }
    
    @GetMapping("/explore-destinations")
    public String destinations(Model model) {
        model.addAttribute("title", "Explore Destinations - TravelLust");
        model.addAttribute("activeTab", "destinations");
        return "destinations";
    }
    
    @GetMapping("/explore-packages")
    public String packages(Model model) {
        model.addAttribute("title", "Tour Packages - TravelLust");
        model.addAttribute("activeTab", "packages");
        return "packages";
    }
    
    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("title", "About Us - TravelLust");
        model.addAttribute("activeTab", "about");
        return "about";
    }
    
    @GetMapping("/testimonials")
    public String testimonials(Model model) {
        model.addAttribute("title", "Testimonials - TravelLust");
        model.addAttribute("activeTab", "testimonials");
        return "testimonials";
    }
    
    @GetMapping("/contact")
    public String contact(Model model) {
        model.addAttribute("title", "Contact Us - TravelLust");
        model.addAttribute("activeTab", "contact");
        return "contact";
    }
    
    // Handler for destination details
    @GetMapping("/destination/{id}")
    public String destinationDetails(@PathVariable("id") String id, Model model) {
        model.addAttribute("title", "Destination Details - TravelLust");
        model.addAttribute("activeTab", "destinations");
        // In a real application, you would fetch destination details from a database
        // For now, we'll just return to the destinations page
        return "destinations";
    }
    
    // Handler for package details
    @GetMapping("/package/{id}")
    public String packageDetails(@PathVariable("id") String id, Model model) {
        model.addAttribute("title", "Package Details - TravelLust");
        model.addAttribute("activeTab", "packages");
        // In a real application, you would fetch package details from a database
        // For now, we'll just return to the packages page
        return "packages";
    }
    
    // Handler for booking functionality
    @GetMapping("/book-now/{type}/{id}")
    public String bookNow(@PathVariable("type") String type, @PathVariable("id") String id, Model model) {
        model.addAttribute("title", "Book Now - TravelLust");
        // In a real application, you would show a booking form
        // For now, we'll just redirect to the contact page
        return "redirect:/contact";
    }
} 