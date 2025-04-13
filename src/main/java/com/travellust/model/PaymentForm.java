package com.travellust.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Model class to represent the payment form data
 */
public class PaymentForm {

    // Booking reference
    private String bookingId;

    // Payment method selection (creditCard, paypal, applePay)
    @NotBlank(message = "Payment method is required")
    private String paymentMethod = "creditCard";

    // Credit card information
    @NotBlank(message = "Cardholder name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String cardholderName;

    @NotBlank(message = "Card number is required")
    @Pattern(regexp = "^\\d{4} \\d{4} \\d{4} \\d{4}$", message = "Invalid card number format")
    private String cardNumber;

    @NotBlank(message = "Expiry date is required")
    @Pattern(regexp = "^\\d{2}/\\d{2}$", message = "Invalid expiry date format (MM/YY)")
    private String expiryDate;

    @NotBlank(message = "CVV is required")
    @Pattern(regexp = "^\\d{3,4}$", message = "Invalid CVV")
    private String cvv;

    // Billing address (optional if sameAsContact is true)
    private boolean sameAsContact = true;
    private String billingName;
    private String billingAddress;
    private String billingCity;
    private String billingState;
    private String billingZip;
    private String billingCountry;

    // Additional options
    private boolean addTravelInsurance;

    // Getters and Setters

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getCardholderName() {
        return cardholderName;
    }

    public void setCardholderName(String cardholderName) {
        this.cardholderName = cardholderName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public boolean isSameAsContact() {
        return sameAsContact;
    }

    public void setSameAsContact(boolean sameAsContact) {
        this.sameAsContact = sameAsContact;
    }

    public String getBillingName() {
        return billingName;
    }

    public void setBillingName(String billingName) {
        this.billingName = billingName;
    }

    public String getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }

    public String getBillingCity() {
        return billingCity;
    }

    public void setBillingCity(String billingCity) {
        this.billingCity = billingCity;
    }

    public String getBillingState() {
        return billingState;
    }

    public void setBillingState(String billingState) {
        this.billingState = billingState;
    }

    public String getBillingZip() {
        return billingZip;
    }

    public void setBillingZip(String billingZip) {
        this.billingZip = billingZip;
    }

    public String getBillingCountry() {
        return billingCountry;
    }

    public void setBillingCountry(String billingCountry) {
        this.billingCountry = billingCountry;
    }

    public boolean isAddTravelInsurance() {
        return addTravelInsurance;
    }

    public void setAddTravelInsurance(boolean addTravelInsurance) {
        this.addTravelInsurance = addTravelInsurance;
    }

    @Override
    public String toString() {
        return "PaymentForm{" +
                "bookingId='" + bookingId + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", cardholderName='" + cardholderName + '\'' +
                ", cardNumber='[MASKED]'" +
                ", expiryDate='[MASKED]'" +
                ", cvv='[MASKED]'" +
                ", sameAsContact=" + sameAsContact +
                ", addTravelInsurance=" + addTravelInsurance +
                '}';
    }
} 