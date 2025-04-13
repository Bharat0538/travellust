package com.travellust.controller;

import com.travellust.dto.TourPackageDto;
import com.travellust.model.PackageType;
import com.travellust.service.DestinationService;
import com.travellust.service.TourPackageService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/tour-packages")
public class TourPackageController {
    
    private final TourPackageService tourPackageService;
    private final DestinationService destinationService;
    
    @GetMapping
    public String getAllPackages(
            @RequestParam(required = false) Long destinationId,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) Integer maxDuration,
            @RequestParam(required = false) String country,
            Model model) {
        
        List<TourPackageDto> packages;
        PackageType packageType = null;
        
        if (type != null && !type.isEmpty()) {
            try {
                packageType = PackageType.valueOf(type);
            } catch (IllegalArgumentException e) {
                // Invalid enum value, will be handled in the filter conditions
            }
        }
        
        if (destinationId != null) {
            packages = tourPackageService.getTourPackagesByDestination(destinationId);
            model.addAttribute("filterDestinationId", destinationId);
            model.addAttribute("filterDestinationName", destinationService.getDestinationById(destinationId).getName());
        } else if (packageType != null) {
            packages = tourPackageService.getPackagesByType(packageType);
            model.addAttribute("filterType", packageType);
        } else if (minPrice != null && maxPrice != null) {
            packages = tourPackageService.getPackagesByPriceRange(minPrice, maxPrice);
            model.addAttribute("filterMinPrice", minPrice);
            model.addAttribute("filterMaxPrice", maxPrice);
        } else if (startDate != null && endDate != null) {
            packages = tourPackageService.getPackagesByDateRange(startDate, endDate);
            model.addAttribute("filterStartDate", startDate);
            model.addAttribute("filterEndDate", endDate);
        } else if (maxDuration != null) {
            packages = tourPackageService.getPackagesByMaxDuration(maxDuration);
            model.addAttribute("filterMaxDuration", maxDuration);
        } else if (country != null && packageType != null) {
            packages = tourPackageService.getPackagesByDestinationCountryAndType(country, packageType);
            model.addAttribute("filterCountry", country);
            model.addAttribute("filterType", packageType);
        } else {
            packages = tourPackageService.getAllTourPackages();
        }
        
        model.addAttribute("packages", packages);
        model.addAttribute("packageTypes", PackageType.values());
        model.addAttribute("countries", destinationService.getAllCountries());
        return "packages/list";
    }
    
    @GetMapping("/{id}")
    public String getPackageDetails(@PathVariable Long id, Model model) {
        TourPackageDto tourPackage = tourPackageService.getTourPackageById(id);
        model.addAttribute("package", tourPackage);
        return "packages/details";
    }
    
    @GetMapping("/destination/{id}")
    public ResponseEntity<List<TourPackageDto>> getPackagesByDestination(@PathVariable Long id) {
        List<TourPackageDto> packages = tourPackageService.getTourPackagesByDestination(id);
        return ResponseEntity.ok(packages);
    }
    
    @GetMapping("/featured")
    public ResponseEntity<List<TourPackageDto>> getFeaturedPackages() {
        List<TourPackageDto> packages = tourPackageService.getFeaturedTourPackages();
        return ResponseEntity.ok(packages);
    }
    
    @GetMapping("/discounts")
    public String getPackagesWithDiscount(Model model) {
        List<TourPackageDto> discountedPackages = tourPackageService.getPackagesWithDiscount();
        model.addAttribute("packages", discountedPackages);
        model.addAttribute("discounted", true);
        return "packages/list";
    }
    
    // Admin-only endpoints for managing tour packages
    
    @GetMapping("/admin/create")
    public String showCreateForm(Model model) {
        model.addAttribute("tourPackage", new TourPackageDto());
        model.addAttribute("packageTypes", PackageType.values());
        model.addAttribute("destinations", destinationService.getAllDestinations());
        return "admin/packages/create";
    }
    
    @PostMapping("/admin/create")
    public String createPackage(@ModelAttribute TourPackageDto tourPackageDto) {
        tourPackageService.createTourPackage(tourPackageDto);
        return "redirect:/tour-packages";
    }
    
    @GetMapping("/admin/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        TourPackageDto tourPackage = tourPackageService.getTourPackageById(id);
        model.addAttribute("tourPackage", tourPackage);
        model.addAttribute("packageTypes", PackageType.values());
        model.addAttribute("destinations", destinationService.getAllDestinations());
        return "admin/packages/edit";
    }
    
    @PostMapping("/admin/edit/{id}")
    public String updatePackage(@PathVariable Long id, @ModelAttribute TourPackageDto tourPackageDto) {
        tourPackageService.updateTourPackage(id, tourPackageDto);
        return "redirect:/tour-packages/" + id;
    }
    
    @PostMapping("/admin/delete/{id}")
    public String deletePackage(@PathVariable Long id) {
        tourPackageService.deleteTourPackage(id);
        return "redirect:/tour-packages";
    }
} 