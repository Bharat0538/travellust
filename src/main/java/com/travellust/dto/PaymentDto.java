package com.travellust.dto;

import com.travellust.model.PaymentMethod;
import com.travellust.model.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDto {
    private Long id;
    private LocalDateTime paymentDate;
    private BigDecimal amount;
    private String transactionId;
    private PaymentMethod paymentMethod;
    private PaymentStatus status;
    private String paymentDetails;
    private Long bookingId;
    
    private String customerName;
    private String customerEmail;
} 