package com.travellust.service;

import com.travellust.dto.PaymentDto;
import com.travellust.model.PaymentMethod;
import com.travellust.model.PaymentStatus;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service interface for payment operations
 */
public interface PaymentService {
    
    List<PaymentDto> getAllPayments();
    
    PaymentDto getPaymentById(Long id);
    
    PaymentDto getPaymentByTransactionId(String transactionId);
    
    PaymentDto createPayment(PaymentDto paymentDto);
    
    PaymentDto updatePayment(Long id, PaymentDto paymentDto);
    
    void deletePayment(Long id);
    
    List<PaymentDto> getPaymentsByBookingId(Long bookingId);
    
    List<PaymentDto> getPaymentsByStatus(PaymentStatus status);
    
    List<PaymentDto> getPaymentsByMethod(PaymentMethod paymentMethod);
    
    List<PaymentDto> getPaymentsByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    List<PaymentDto> getPaymentsByUserId(Long userId);
    
    /**
     * Process payment for a booking
     * 
     * @param bookingId The booking ID
     * @param paymentMethod The payment method
     * @param paymentDetails Additional payment details (e.g., transaction ID for bank transfers)
     * @return true if payment was successful, false otherwise
     * @throws Exception if an error occurs during payment processing
     */
    boolean processPayment(Long bookingId, PaymentMethod paymentMethod, String paymentDetails) throws Exception;
    
    PaymentDto updatePaymentStatus(Long id, PaymentStatus status);
    
    /**
     * Get payment status for a booking
     * 
     * @param bookingId The booking ID
     * @return true if payment has been completed, false otherwise
     */
    boolean isPaymentCompleted(Long bookingId);
    
    /**
     * Refund payment for a booking
     * 
     * @param bookingId The booking ID
     * @param amount The amount to refund (can be less than the total payment amount)
     * @return true if refund was successful, false otherwise
     * @throws Exception if an error occurs during refund processing
     */
    boolean refundPayment(Long bookingId, double amount) throws Exception;
} 