package com.travellust.repository;

import com.travellust.model.Destination;
import com.travellust.model.DestinationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DestinationRepository extends JpaRepository<Destination, Long> {
    
    List<Destination> findByFeaturedTrue();
    
    List<Destination> findByType(DestinationType type);
    
    List<Destination> findByCountry(String country);
    
    @Query("SELECT DISTINCT d.country FROM Destination d ORDER BY d.country")
    List<String> findAllCountries();
    
    List<Destination> findByNameContainingIgnoreCase(String name);
    
    @Query("SELECT d FROM Destination d WHERE d.averageRating >= :minRating")
    List<Destination> findByMinimumRating(Double minRating);
} 