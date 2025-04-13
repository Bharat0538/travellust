package com.travellust.service.impl;

import com.travellust.dto.BookingDto;
import com.travellust.dto.PaymentDto;
import com.travellust.model.Booking;
import com.travellust.model.BookingStatus;
import com.travellust.model.PaymentStatus;
import com.travellust.model.TourPackage;
import com.travellust.model.User;
import com.travellust.repository.BookingRepository;
import com.travellust.repository.TourPackageRepository;
import com.travellust.repository.UserRepository;
import com.travellust.service.BookingService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final TourPackageRepository tourPackageRepository;
    
    @Override
    public List<BookingDto> getAllBookings() {
        return bookingRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public BookingDto getBookingById(Long id) {
        return bookingRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found with id: " + id));
    }
    
    @Override
    public BookingDto getBookingByReference(String bookingReference) {
        return bookingRepository.findByBookingReference(bookingReference)
                .map(this::convertToDto)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found with reference: " + bookingReference));
    }
    
    @Override
    @Transactional
    public BookingDto createBooking(BookingDto bookingDto) {
        Booking booking = convertToEntity(bookingDto);
        
        // Generate unique booking reference
        booking.setBookingReference(generateBookingReference());
        
        // Set booking date to current time
        booking.setBookingDate(LocalDateTime.now());
        
        // Default status for new bookings
        booking.setStatus(BookingStatus.PENDING);
        
        Booking savedBooking = bookingRepository.save(booking);
        return convertToDto(savedBooking);
    }
    
    @Override
    @Transactional
    public BookingDto updateBooking(Long id, BookingDto bookingDto) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found with id: " + id));
        
        updateBookingFromDto(booking, bookingDto);
        
        Booking updatedBooking = bookingRepository.save(booking);
        return convertToDto(updatedBooking);
    }
    
    @Override
    @Transactional
    public void deleteBooking(Long id) {
        if (!bookingRepository.existsById(id)) {
            throw new EntityNotFoundException("Booking not found with id: " + id);
        }
        bookingRepository.deleteById(id);
    }
    
    @Override
    public List<BookingDto> getBookingsByUser(Long userId) {
        return bookingRepository.findByUserId(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<BookingDto> getBookingsByPackage(Long packageId) {
        return bookingRepository.findByTourPackageId(packageId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<BookingDto> getBookingsByStatus(BookingStatus status) {
        return bookingRepository.findByStatus(status).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<BookingDto> getBookingsByTravelDateRange(LocalDate startDate, LocalDate endDate) {
        return bookingRepository.findByTravelDateBetween(startDate, endDate).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<BookingDto> getBookingsByUserAndStatus(Long userId, BookingStatus status) {
        return bookingRepository.findByUserIdAndStatus(userId, status).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<BookingDto> getBookingsByUsername(String username) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + username));
        return getBookingsByUser(user.getId());
    }
    
    @Override
    public boolean isPackageAvailableForDate(Long packageId, LocalDate date, Integer requestedPeople) {
        TourPackage tourPackage = tourPackageRepository.findById(packageId)
                .orElseThrow(() -> new EntityNotFoundException("Tour package not found with id: " + packageId));
        
        // Check if the date is within the package's availability range
        if (tourPackage.getStartDate() != null && tourPackage.getEndDate() != null) {
            if (date.isBefore(tourPackage.getStartDate()) || date.isAfter(tourPackage.getEndDate())) {
                return false;
            }
        }
        
        // Check group size availability
        if (tourPackage.getMaxGroupSize() != null) {
            Long currentBookingsCount = bookingRepository.countBookingsByPackageAndDate(packageId, date);
            return (currentBookingsCount + requestedPeople) <= tourPackage.getMaxGroupSize();
        }
        
        return true;
    }
    
    @Override
    @Transactional
    public BookingDto updateBookingStatus(Long id, String status) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found with id: " + id));
        
        booking.setStatus(BookingStatus.valueOf(status));
        Booking updatedBooking = bookingRepository.save(booking);
        return convertToDto(updatedBooking);
    }
    
    @Override
    public BookingDto convertToDto(Booking booking) {
        PaymentDto paymentDto = null;
        if (booking.getPayment() != null) {
            paymentDto = PaymentDto.builder()
                    .id(booking.getPayment().getId())
                    .paymentDate(booking.getPayment().getPaymentDate())
                    .amount(booking.getPayment().getAmount())
                    .transactionId(booking.getPayment().getTransactionId())
                    .paymentMethod(booking.getPayment().getPaymentMethod())
                    .status(booking.getPayment().getStatus())
                    .paymentDetails(booking.getPayment().getPaymentDetails())
                    .bookingId(booking.getId())
                    .build();
        }
        
        return BookingDto.builder()
                .id(booking.getId())
                .bookingDate(booking.getBookingDate().toLocalDate())
                .travelDate(booking.getTravelDate())
                .numberOfPeople(booking.getNumberOfPeople())
                .totalAmount(booking.getTotalAmount())
                .bookingReference(booking.getBookingReference())
                .specialRequests(booking.getSpecialRequests())
                .status(booking.getStatus())
                .userId(booking.getUser().getId())
                .userName(booking.getUser().getName())
                .packageId(booking.getTourPackage().getId())
                .packageName(booking.getTourPackage().getName())
                .destinationName(booking.getTourPackage().getDestination() != null ? 
                        booking.getTourPackage().getDestination().getName() : null)
                .payment(paymentDto)
                .contactName(booking.getContactName())
                .contactEmail(booking.getContactEmail())
                .contactPhone(booking.getContactPhone())
                .build();
    }
    
    @Override
    public Booking convertToEntity(BookingDto dto) {
        Booking booking = new Booking();
        
        if (dto.getId() != null) {
            booking.setId(dto.getId());
        }
        
        // Find user and package
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + dto.getUserId()));
        
        TourPackage tourPackage = tourPackageRepository.findById(dto.getPackageId())
                .orElseThrow(() -> new EntityNotFoundException("Tour package not found with id: " + dto.getPackageId()));
        
        booking.setBookingDate(LocalDateTime.now());
        booking.setTravelDate(dto.getTravelDate());
        booking.setNumberOfPeople(dto.getNumberOfPeople());
        booking.setTotalAmount(dto.getTotalAmount());
        booking.setBookingReference(dto.getBookingReference());
        booking.setSpecialRequests(dto.getSpecialRequests());
        booking.setStatus(dto.getStatus() != null ? dto.getStatus() : BookingStatus.PENDING);
        booking.setUser(user);
        booking.setTourPackage(tourPackage);
        booking.setContactName(dto.getContactName());
        booking.setContactEmail(dto.getContactEmail());
        booking.setContactPhone(dto.getContactPhone());
        
        return booking;
    }
    
    private void updateBookingFromDto(Booking booking, BookingDto dto) {
        if (dto.getTravelDate() != null) {
            booking.setTravelDate(dto.getTravelDate());
        }
        
        if (dto.getNumberOfPeople() != null) {
            booking.setNumberOfPeople(dto.getNumberOfPeople());
            
            // Recalculate total amount if number of people changed
            BigDecimal discountedPrice = booking.getTourPackage().getDiscountedPrice();
            booking.setTotalAmount(discountedPrice.multiply(BigDecimal.valueOf(booking.getNumberOfPeople())));
        }
        
        if (dto.getSpecialRequests() != null) {
            booking.setSpecialRequests(dto.getSpecialRequests());
        }
        
        if (dto.getStatus() != null) {
            booking.setStatus(dto.getStatus());
        }
        
        // Update contact information
        if (dto.getContactName() != null) {
            booking.setContactName(dto.getContactName());
        }
        
        if (dto.getContactEmail() != null) {
            booking.setContactEmail(dto.getContactEmail());
        }
        
        if (dto.getContactPhone() != null) {
            booking.setContactPhone(dto.getContactPhone());
        }
        
        if (dto.getPackageId() != null && !dto.getPackageId().equals(booking.getTourPackage().getId())) {
            TourPackage newTourPackage = tourPackageRepository.findById(dto.getPackageId())
                    .orElseThrow(() -> new EntityNotFoundException("Tour package not found with id: " + dto.getPackageId()));
            booking.setTourPackage(newTourPackage);
            
            // Recalculate total amount if package changed
            BigDecimal discountedPrice = newTourPackage.getDiscountedPrice();
            booking.setTotalAmount(discountedPrice.multiply(BigDecimal.valueOf(booking.getNumberOfPeople())));
        }
    }
    
    /**
     * Generate a unique booking reference number
     * 
     * @return A unique booking reference
     */
    private String generateBookingReference() {
        // Format: TL-xxxxxxxx (where x is alphanumeric)
        return "TL-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
} 