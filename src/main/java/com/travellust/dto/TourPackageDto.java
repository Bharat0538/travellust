package com.travellust.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.travellust.model.PackageType;

/**
 * DTO for transferring tour package data between layers
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TourPackageDto {

    private Long id;
    private String name;
    private Long destinationId;
    private String destinationName;
    private String destinationCountry;
    private String description;
    private Integer duration;
    private Double price;
    private Double discountPercentage;
    private Double discountedPrice;
    private String imageUrl;
    private PackageType packageType;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer maxGroupSize;
    private Boolean featured;
    private String inclusionsSummary;
    private String exclusionsSummary;
    private String itinerarySummary;
    
    // Getters and Setters
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Long getDestinationId() {
        return destinationId;
    }
    
    public void setDestinationId(Long destinationId) {
        this.destinationId = destinationId;
    }
    
    public String getDestinationName() {
        return destinationName;
    }
    
    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }
    
    public String getDestinationCountry() {
        return destinationCountry;
    }
    
    public void setDestinationCountry(String destinationCountry) {
        this.destinationCountry = destinationCountry;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Integer getDuration() {
        return duration;
    }
    
    public void setDuration(Integer duration) {
        this.duration = duration;
    }
    
    public Double getPrice() {
        return price;
    }
    
    public void setPrice(Double price) {
        this.price = price;
    }
    
    public Double getDiscountPercentage() {
        return discountPercentage;
    }
    
    public void setDiscountPercentage(Double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }
    
    public Double getDiscountedPrice() {
        if (discountPercentage != null && discountPercentage > 0) {
            return price - (price * discountPercentage / 100);
        }
        return price;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    public PackageType getPackageType() {
        return packageType;
    }
    
    public void setPackageType(PackageType packageType) {
        this.packageType = packageType;
    }
    
    public LocalDate getStartDate() {
        return startDate;
    }
    
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    
    public LocalDate getEndDate() {
        return endDate;
    }
    
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
    
    public Integer getMaxGroupSize() {
        return maxGroupSize;
    }
    
    public void setMaxGroupSize(Integer maxGroupSize) {
        this.maxGroupSize = maxGroupSize;
    }
    
    public Boolean getFeatured() {
        return featured;
    }
    
    public void setFeatured(Boolean featured) {
        this.featured = featured;
    }
    
    public String getInclusionsSummary() {
        return inclusionsSummary;
    }
    
    public void setInclusionsSummary(String inclusionsSummary) {
        this.inclusionsSummary = inclusionsSummary;
    }
    
    public String getExclusionsSummary() {
        return exclusionsSummary;
    }
    
    public void setExclusionsSummary(String exclusionsSummary) {
        this.exclusionsSummary = exclusionsSummary;
    }
    
    public String getItinerarySummary() {
        return itinerarySummary;
    }
    
    public void setItinerarySummary(String itinerarySummary) {
        this.itinerarySummary = itinerarySummary;
    }

    public boolean isFeatured() {
        return featured != null && featured;
    }
} 