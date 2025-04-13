package com.travellust.service.impl;

import com.travellust.dto.PaymentDto;
import com.travellust.model.Booking;
import com.travellust.model.BookingStatus;
import com.travellust.model.Payment;
import com.travellust.model.PaymentMethod;
import com.travellust.model.PaymentStatus;
import com.travellust.repository.BookingRepository;
import com.travellust.repository.PaymentRepository;
import com.travellust.service.PaymentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    
    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    
    @Override
    public List<PaymentDto> getAllPayments() {
        return paymentRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public PaymentDto getPaymentById(Long id) {
        return paymentRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found with id: " + id));
    }
    
    @Override
    public PaymentDto getPaymentByTransactionId(String transactionId) {
        return paymentRepository.findByTransactionId(transactionId)
                .map(this::convertToDto)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found with transaction id: " + transactionId));
    }
    
    @Override
    @Transactional
    public PaymentDto createPayment(PaymentDto paymentDto) {
        Payment payment = convertToEntity(paymentDto);
        payment.setPaymentDate(LocalDateTime.now());
        Payment savedPayment = paymentRepository.save(payment);
        return convertToDto(savedPayment);
    }
    
    @Override
    @Transactional
    public PaymentDto updatePayment(Long id, PaymentDto paymentDto) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found with id: " + id));
        
        updatePaymentFromDto(payment, paymentDto);
        
        Payment updatedPayment = paymentRepository.save(payment);
        return convertToDto(updatedPayment);
    }
    
    @Override
    @Transactional
    public void deletePayment(Long id) {
        if (!paymentRepository.existsById(id)) {
            throw new EntityNotFoundException("Payment not found with id: " + id);
        }
        paymentRepository.deleteById(id);
    }
    
    @Override
    public List<PaymentDto> getPaymentsByBookingId(Long bookingId) {
        return paymentRepository.findByBookingId(bookingId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<PaymentDto> getPaymentsByStatus(PaymentStatus status) {
        return paymentRepository.findByStatus(status).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<PaymentDto> getPaymentsByMethod(PaymentMethod paymentMethod) {
        return paymentRepository.findByPaymentMethod(paymentMethod).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<PaymentDto> getPaymentsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return paymentRepository.findByPaymentDateBetween(startDate, endDate).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<PaymentDto> getPaymentsByUserId(Long userId) {
        return paymentRepository.findByUserId(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public boolean processPayment(Long bookingId, PaymentMethod paymentMethod, String paymentDetails) throws Exception {
        try {
            Booking booking = bookingRepository.findById(bookingId)
                    .orElseThrow(() -> new EntityNotFoundException("Booking not found with id: " + bookingId));
            
            // Create a new payment
            Payment payment = new Payment();
            payment.setAmount(booking.getTotalAmount());
            payment.setPaymentDate(LocalDateTime.now());
            payment.setPaymentMethod(paymentMethod);
            payment.setStatus(PaymentStatus.PENDING); // Initial status
            payment.setPaymentDetails(paymentDetails);
            payment.setTransactionId(generateTransactionId());
            payment.setBooking(booking);
            
            Payment savedPayment = paymentRepository.save(payment);
            
            // Update booking status
            booking.setStatus(BookingStatus.CONFIRMED);
            bookingRepository.save(booking);
            
            // Simulate payment processing
            // In a real application, this would involve integrating with a payment gateway
            simulatePaymentProcessing(savedPayment);
            
            return true;
        } catch (Exception e) {
            throw new Exception("Payment processing failed: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional
    public boolean refundPayment(Long bookingId, double amount) throws Exception {
        try {
            Booking booking = bookingRepository.findById(bookingId)
                    .orElseThrow(() -> new EntityNotFoundException("Booking not found with id: " + bookingId));
            
            Payment payment = paymentRepository.findByBookingId(bookingId).stream()
                    .filter(p -> p.getStatus() == PaymentStatus.COMPLETED)
                    .findFirst()
                    .orElseThrow(() -> new Exception("No completed payment found for booking id: " + bookingId));
            
            if (amount <= 0 || amount > payment.getAmount().doubleValue()) {
                throw new Exception("Invalid refund amount: " + amount);
            }
            
            // Process refund (in a real app, this would call a payment gateway)
            payment.setStatus(amount == payment.getAmount().doubleValue() 
                    ? PaymentStatus.REFUNDED : PaymentStatus.PARTIALLY_REFUNDED);
            
            paymentRepository.save(payment);
            
            // Update booking status if it's a full refund
            if (amount == payment.getAmount().doubleValue()) {
                booking.setStatus(BookingStatus.REFUNDED);
                bookingRepository.save(booking);
            }
            
            return true;
        } catch (Exception e) {
            throw new Exception("Refund processing failed: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional
    public PaymentDto updatePaymentStatus(Long id, PaymentStatus status) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found with id: " + id));
        
        payment.setStatus(status);
        
        // If payment is completed, update booking status
        if (status == PaymentStatus.COMPLETED) {
            Booking booking = payment.getBooking();
            booking.setStatus(BookingStatus.PAID);
            bookingRepository.save(booking);
        } else if (status == PaymentStatus.FAILED) {
            Booking booking = payment.getBooking();
            booking.setStatus(BookingStatus.PENDING);
            bookingRepository.save(booking);
        }
        
        Payment updatedPayment = paymentRepository.save(payment);
        return convertToDto(updatedPayment);
    }
    
    @Override
    public boolean isPaymentCompleted(Long bookingId) {
        return paymentRepository.findByBookingId(bookingId).stream()
                .anyMatch(payment -> payment.getStatus() == PaymentStatus.COMPLETED);
    }
    
    private PaymentDto convertToDto(Payment payment) {
        return PaymentDto.builder()
                .id(payment.getId())
                .paymentDate(payment.getPaymentDate())
                .amount(payment.getAmount())
                .transactionId(payment.getTransactionId())
                .paymentMethod(payment.getPaymentMethod())
                .status(payment.getStatus())
                .paymentDetails(payment.getPaymentDetails())
                .bookingId(payment.getBooking().getId())
                .build();
    }
    
    private Payment convertToEntity(PaymentDto dto) {
        Payment payment = new Payment();
        
        if (dto.getId() != null) {
            payment.setId(dto.getId());
        }
        
        payment.setPaymentDate(dto.getPaymentDate() != null ? dto.getPaymentDate() : LocalDateTime.now());
        payment.setAmount(dto.getAmount());
        payment.setTransactionId(dto.getTransactionId() != null ? dto.getTransactionId() : generateTransactionId());
        payment.setPaymentMethod(dto.getPaymentMethod());
        payment.setStatus(dto.getStatus() != null ? dto.getStatus() : PaymentStatus.PENDING);
        payment.setPaymentDetails(dto.getPaymentDetails());
        
        if (dto.getBookingId() != null) {
            Booking booking = bookingRepository.findById(dto.getBookingId())
                    .orElseThrow(() -> new EntityNotFoundException("Booking not found with id: " + dto.getBookingId()));
            payment.setBooking(booking);
        }
        
        return payment;
    }
    
    private void updatePaymentFromDto(Payment payment, PaymentDto dto) {
        if (dto.getPaymentMethod() != null) {
            payment.setPaymentMethod(dto.getPaymentMethod());
        }
        
        if (dto.getStatus() != null) {
            payment.setStatus(dto.getStatus());
            
            // If payment is completed, update booking status
            if (dto.getStatus() == PaymentStatus.COMPLETED) {
                Booking booking = payment.getBooking();
                booking.setStatus(BookingStatus.PAID);
                bookingRepository.save(booking);
            } else if (dto.getStatus() == PaymentStatus.FAILED) {
                Booking booking = payment.getBooking();
                booking.setStatus(BookingStatus.PENDING);
                bookingRepository.save(booking);
            }
        }
        
        if (dto.getPaymentDetails() != null) {
            payment.setPaymentDetails(dto.getPaymentDetails());
        }
    }
    
    private String generateTransactionId() {
        return "TXN" + UUID.randomUUID().toString().replace("-", "").substring(0, 16).toUpperCase();
    }
    
    // Simulate payment processing (in a real app, this would interact with payment gateway API)
    private void simulatePaymentProcessing(Payment payment) {
        // Simulate a successful payment (for demo purposes)
        payment.setStatus(PaymentStatus.COMPLETED);
        paymentRepository.save(payment);
        
        // Update booking status 
        Booking booking = payment.getBooking();
        booking.setStatus(BookingStatus.PAID);
        bookingRepository.save(booking);
    }
} 