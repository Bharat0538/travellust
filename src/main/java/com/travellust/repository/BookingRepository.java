package com.travellust.repository;

import com.travellust.model.Booking;
import com.travellust.model.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    
    List<Booking> findByUserId(Long userId);
    
    List<Booking> findByTourPackageId(Long packageId);
    
    Optional<Booking> findByBookingReference(String bookingReference);
    
    List<Booking> findByStatus(BookingStatus status);
    
    @Query("SELECT b FROM Booking b WHERE b.travelDate BETWEEN :startDate AND :endDate")
    List<Booking> findByTravelDateBetween(LocalDate startDate, LocalDate endDate);
    
    @Query("SELECT b FROM Booking b WHERE b.user.id = :userId AND b.status = :status")
    List<Booking> findByUserIdAndStatus(Long userId, BookingStatus status);
    
    @Query("SELECT COUNT(b) FROM Booking b WHERE b.tourPackage.id = :packageId AND b.travelDate = :date")
    Long countBookingsByPackageAndDate(Long packageId, LocalDate date);
} 