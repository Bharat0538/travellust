package com.travellust.repository;

import com.travellust.model.PackageType;
import com.travellust.model.TourPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface TourPackageRepository extends JpaRepository<TourPackage, Long> {
    
    List<TourPackage> findByFeaturedTrue();
    
    List<TourPackage> findByDestinationId(Long destinationId);
    
    List<TourPackage> findByPackageType(PackageType packageType);
    
    @Query("SELECT tp FROM TourPackage tp WHERE tp.price BETWEEN :minPrice AND :maxPrice")
    List<TourPackage> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);
    
    @Query("SELECT tp FROM TourPackage tp WHERE tp.startDate >= :startDate AND tp.endDate <= :endDate")
    List<TourPackage> findByDateRange(LocalDate startDate, LocalDate endDate);
    
    @Query("SELECT tp FROM TourPackage tp WHERE tp.duration <= :maxDuration")
    List<TourPackage> findByMaxDuration(Integer maxDuration);
    
    @Query("SELECT tp FROM TourPackage tp WHERE tp.discountPercentage > 0")
    List<TourPackage> findWithDiscount();
    
    List<TourPackage> findByDestination_CountryAndPackageType(String country, PackageType packageType);
} 