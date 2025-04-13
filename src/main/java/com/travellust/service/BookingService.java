package com.travellust.service;

import java.time.LocalDate;
import java.util.List;

import com.travellust.dto.BookingDto;
import com.travellust.model.BookingStatus;
import com.travellust.model.Booking;

/**
 * Service interface for managing booking operations
 */
public interface BookingService {

    /**
     * Get all bookings
     * 
     * @return List of all bookings
     */
    List<BookingDto> getAllBookings();
    
    /**
     * Get bookings by user ID
     * 
     * @param userId The user ID
     * @return List of bookings for the specified user
     */
    List<BookingDto> getBookingsByUser(Long userId);
    
    /**
     * Get booking by ID
     * 
     * @param id The booking ID
     * @return The booking with the specified ID
     */
    BookingDto getBookingById(Long id);
    
    /**
     * Get booking by reference number
     * 
     * @param reference The booking reference number
     * @return The booking with the specified reference number
     */
    BookingDto getBookingByReference(String reference);
    
    /**
     * Create a new booking
     * 
     * @param bookingDto The booking data
     * @return The created booking
     */
    BookingDto createBooking(BookingDto bookingDto);
    
    /**
     * Update booking status
     * 
     * @param id The booking ID
     * @param status The new status
     * @return The updated booking
     */
    BookingDto updateBookingStatus(Long id, String status);
    
    /**
     * Get bookings by status
     * 
     * @param status The booking status
     * @return List of bookings with the specified status
     */
    List<BookingDto> getBookingsByStatus(BookingStatus status);
    
    /**
     * Get bookings by travel date range
     * 
     * @param startDate The start date
     * @param endDate The end date
     * @return List of bookings within the specified travel date range
     */
    List<BookingDto> getBookingsByTravelDateRange(LocalDate startDate, LocalDate endDate);
    
    /**
     * Check if a package is available for a specific date and number of people
     * 
     * @param packageId The package ID
     * @param date The travel date
     * @param numberOfPeople The number of people
     * @return true if the package is available, false otherwise
     */
    boolean isPackageAvailableForDate(Long packageId, LocalDate date, Integer numberOfPeople);
    
    /**
     * Update an existing booking
     * 
     * @param id The booking ID
     * @param bookingDto The updated booking data
     * @return The updated booking
     */
    BookingDto updateBooking(Long id, BookingDto bookingDto);
    
    /**
     * Delete a booking
     * 
     * @param id The booking ID
     */
    void deleteBooking(Long id);
    
    /**
     * Get bookings by package ID
     * 
     * @param packageId The package ID
     * @return List of bookings for the specified package
     */
    List<BookingDto> getBookingsByPackage(Long packageId);
    
    /**
     * Get bookings by user ID and status
     * 
     * @param userId The user ID
     * @param status The booking status
     * @return List of bookings for the specified user and status
     */
    List<BookingDto> getBookingsByUserAndStatus(Long userId, BookingStatus status);
    
    /**
     * Get all bookings for a user
     * 
     * @param username The username
     * @return List of bookings
     */
    List<BookingDto> getBookingsByUsername(String username);
    
    /**
     * Convert a Booking entity to a BookingDto
     * 
     * @param booking The booking entity
     * @return The booking DTO
     */
    BookingDto convertToDto(Booking booking);
    
    /**
     * Convert a BookingDto to a Booking entity
     * 
     * @param bookingDto The booking DTO
     * @return The booking entity
     */
    Booking convertToEntity(BookingDto bookingDto);
} 