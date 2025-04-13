package com.travellust.service;

import java.util.List;

import com.travellust.dto.TourPackageDto;
import com.travellust.model.PackageType;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Service interface for tour package operations
 */
public interface TourPackageService {

    /**
     * Get all tour packages
     * 
     * @return List of all tour packages
     */
    List<TourPackageDto> getAllTourPackages();
    
    /**
     * Get tour package by ID
     * 
     * @param id The tour package ID
     * @return The tour package with the specified ID
     */
    TourPackageDto getTourPackageById(Long id);
    
    /**
     * Get tour packages by destination ID
     * 
     * @param destinationId The destination ID
     * @return List of tour packages for the specified destination
     */
    List<TourPackageDto> getTourPackagesByDestination(Long destinationId);
    
    /**
     * Get featured tour packages
     * 
     * @return List of featured tour packages
     */
    List<TourPackageDto> getFeaturedTourPackages();
    
    /**
     * Search tour packages
     * 
     * @param searchTerm The search term
     * @return List of tour packages matching the search term
     */
    List<TourPackageDto> searchTourPackages(String searchTerm);
    
    TourPackageDto createTourPackage(TourPackageDto tourPackageDto);
    
    TourPackageDto updateTourPackage(Long id, TourPackageDto tourPackageDto);
    
    void deleteTourPackage(Long id);
    
    List<TourPackageDto> getPackagesByType(PackageType packageType);
    
    List<TourPackageDto> getPackagesByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);
    
    List<TourPackageDto> getPackagesByDateRange(LocalDate startDate, LocalDate endDate);
    
    List<TourPackageDto> getPackagesByMaxDuration(Integer maxDuration);
    
    List<TourPackageDto> getPackagesWithDiscount();
    
    /**
     * Get tour packages by destination country and package type
     * 
     * @param country The country
     * @param packageType The package type
     * @return List of tour packages for the specified destination country and package type
     */
    List<TourPackageDto> getPackagesByDestinationCountryAndType(String country, PackageType packageType);
} 