package com.travellust.model;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Model class to represent the booking form data
 */
public class BookingForm {

    // Package Details
    private Long packageId;
    private String packageName;
    private Double packagePrice;
    private String packageImageUrl;
    
    // Destination Details
    private Long destinationId;
    private String destinationName;
    
    // Travel Information
    @NotNull(message = "Travel date is required")
    private LocalDate travelDate;
    
    @NotNull(message = "Number of people is required")
    @Min(value = 1, message = "At least one person is required")
    private Integer numberOfPeople = 1;
    
    // Contact Information
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String contactName;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String contactEmail;
    
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9+\\-\\s().]{7,20}$", message = "Invalid phone number")
    private String contactPhone;
    
    @NotBlank(message = "Country is required")
    private String country;
    
    // Additional Information
    private String specialRequests;
    private String travelReason;
    private String accommodationPreference;
    
    // Marketing & Terms
    private boolean marketingOptIn;
    
    @NotNull(message = "You must accept the terms and conditions")
    private Boolean termsAgreed;
    
    // Getters and Setters
    
    public Long getPackageId() {
        return packageId;
    }
    
    public void setPackageId(Long packageId) {
        this.packageId = packageId;
    }
    
    public String getPackageName() {
        return packageName;
    }
    
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
    
    public Double getPackagePrice() {
        return packagePrice;
    }
    
    public void setPackagePrice(Double packagePrice) {
        this.packagePrice = packagePrice;
    }
    
    public String getPackageImageUrl() {
        return packageImageUrl;
    }
    
    public void setPackageImageUrl(String packageImageUrl) {
        this.packageImageUrl = packageImageUrl;
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
    
    public LocalDate getTravelDate() {
        return travelDate;
    }
    
    public void setTravelDate(LocalDate travelDate) {
        this.travelDate = travelDate;
    }
    
    public Integer getNumberOfPeople() {
        return numberOfPeople;
    }
    
    public void setNumberOfPeople(Integer numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }
    
    public String getContactName() {
        return contactName;
    }
    
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }
    
    public String getContactEmail() {
        return contactEmail;
    }
    
    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }
    
    public String getContactPhone() {
        return contactPhone;
    }
    
    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }
    
    public String getCountry() {
        return country;
    }
    
    public void setCountry(String country) {
        this.country = country;
    }
    
    public String getSpecialRequests() {
        return specialRequests;
    }
    
    public void setSpecialRequests(String specialRequests) {
        this.specialRequests = specialRequests;
    }
    
    public String getTravelReason() {
        return travelReason;
    }
    
    public void setTravelReason(String travelReason) {
        this.travelReason = travelReason;
    }
    
    public String getAccommodationPreference() {
        return accommodationPreference;
    }
    
    public void setAccommodationPreference(String accommodationPreference) {
        this.accommodationPreference = accommodationPreference;
    }
    
    public boolean isMarketingOptIn() {
        return marketingOptIn;
    }
    
    public void setMarketingOptIn(boolean marketingOptIn) {
        this.marketingOptIn = marketingOptIn;
    }
    
    public Boolean getTermsAgreed() {
        return termsAgreed;
    }
    
    public void setTermsAgreed(Boolean termsAgreed) {
        this.termsAgreed = termsAgreed;
    }

    @Override
    public String toString() {
        return "BookingForm{" +
                "packageId=" + packageId +
                ", packageName='" + packageName + '\'' +
                ", travelDate=" + travelDate +
                ", numberOfPeople=" + numberOfPeople +
                ", contactName='" + contactName + '\'' +
                ", contactEmail='" + contactEmail + '\'' +
                '}';
    }
} 