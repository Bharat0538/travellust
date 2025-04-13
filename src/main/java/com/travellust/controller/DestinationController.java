package com.travellust.controller;

import com.travellust.dto.DestinationDto;
import com.travellust.model.DestinationType;
import com.travellust.service.DestinationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/destinations")
public class DestinationController {
    
    private final DestinationService destinationService;
    
    @GetMapping
    public String getAllDestinations(
            @RequestParam(required = false) DestinationType type,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Double minRating,
            Model model) {
        
        List<DestinationDto> destinations;
        
        if (type != null) {
            destinations = destinationService.getDestinationsByType(type);
            model.addAttribute("filterType", type);
        } else if (country != null && !country.isEmpty()) {
            destinations = destinationService.getDestinationsByCountry(country);
            model.addAttribute("filterCountry", country);
        } else if (keyword != null && !keyword.isEmpty()) {
            destinations = destinationService.searchDestinations(keyword);
            model.addAttribute("filterKeyword", keyword);
        } else if (minRating != null) {
            destinations = destinationService.getDestinationsByMinimumRating(minRating);
            model.addAttribute("filterMinRating", minRating);
        } else {
            destinations = destinationService.getAllDestinations();
        }
        
        model.addAttribute("destinations", destinations);
        model.addAttribute("countries", destinationService.getAllCountries());
        model.addAttribute("types", DestinationType.values());
        return "destinations/list";
    }
    
    @GetMapping("/{id}")
    public String getDestinationDetails(@PathVariable Long id, Model model) {
        DestinationDto destination = destinationService.getDestinationById(id);
        model.addAttribute("destination", destination);
        return "destinations/details";
    }
    
    @GetMapping("/featured")
    public String getFeaturedDestinations(Model model) {
        List<DestinationDto> featuredDestinations = destinationService.getFeaturedDestinations();
        model.addAttribute("destinations", featuredDestinations);
        model.addAttribute("featured", true);
        return "destinations/list";
    }
    
    // Admin-only endpoints for managing destinations
    
    @GetMapping("/admin/create")
    public String showCreateForm(Model model) {
        model.addAttribute("destination", new DestinationDto());
        model.addAttribute("types", DestinationType.values());
        return "admin/destinations/create";
    }
    
    @PostMapping("/admin/create")
    public String createDestination(@ModelAttribute DestinationDto destinationDto) {
        destinationService.createDestination(destinationDto);
        return "redirect:/destinations";
    }
    
    @GetMapping("/admin/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        DestinationDto destination = destinationService.getDestinationById(id);
        model.addAttribute("destination", destination);
        model.addAttribute("types", DestinationType.values());
        return "admin/destinations/edit";
    }
    
    @PostMapping("/admin/edit/{id}")
    public String updateDestination(@PathVariable Long id, @ModelAttribute DestinationDto destinationDto) {
        destinationService.updateDestination(id, destinationDto);
        return "redirect:/destinations/" + id;
    }
    
    @PostMapping("/admin/delete/{id}")
    public String deleteDestination(@PathVariable Long id) {
        destinationService.deleteDestination(id);
        return "redirect:/destinations";
    }
} 