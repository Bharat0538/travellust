package com.travellust.config;

import com.travellust.model.Destination;
import com.travellust.model.DestinationType;
import com.travellust.model.PackageType;
import com.travellust.model.TourPackage;
import com.travellust.repository.DestinationRepository;
import com.travellust.repository.TourPackageRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final DestinationRepository destinationRepository;
    private final TourPackageRepository tourPackageRepository;

    @PostConstruct
    @Transactional
    public void initData() {
        if (destinationRepository.count() == 0) {
            initDestinations();
        }
        
        if (tourPackageRepository.count() == 0) {
            initTourPackages();
        }
    }
    
    private void initDestinations() {
        List<Destination> destinations = List.of(
            createDestination("Bali", "Indonesia", "A paradise island known for its beaches, temples and vibrant culture.", DestinationType.BEACH, 4.8, true, "https://images.unsplash.com/photo-1539367628448-4bc5c9d171c8?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1470&q=80"),
            createDestination("Paris", "France", "The City of Light with iconic landmarks like the Eiffel Tower and Louvre Museum.", DestinationType.CITY, 4.7, true, "https://images.unsplash.com/photo-1502602898657-3e91760cbb34?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1473&q=80"),
            createDestination("Santorini", "Greece", "Famous for stunning sunsets, white-washed buildings and blue domes.", DestinationType.BEACH, 4.9, true, "https://images.unsplash.com/photo-1570077188670-e3a8d69ac5ff?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1374&q=80"),
            createDestination("Swiss Alps", "Switzerland", "Magnificent mountain landscapes perfect for skiing and hiking.", DestinationType.MOUNTAIN, 4.8, true, "https://images.unsplash.com/photo-1531816458010-fb7685eecbcb?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1470&q=80"),
            createDestination("Kyoto", "Japan", "Ancient city with thousands of temples, shrines and traditional gardens.", DestinationType.CULTURAL, 4.7, true, "https://images.unsplash.com/photo-1493976040374-85c8e12f0c0e?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1470&q=80"),
            createDestination("Machu Picchu", "Peru", "The lost city of the Incas, perched high in the Andes mountains.", DestinationType.HISTORICAL, 4.9, true, "https://images.unsplash.com/photo-1526392060635-9d6019884377?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1470&q=80"),
            createDestination("Serengeti", "Tanzania", "Famous for its annual migration of wildlife and home to the Big Five.", DestinationType.WILDLIFE, 4.8, true, "https://images.unsplash.com/photo-1518714049508-ab02028e903d?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1470&q=80"),
            createDestination("Maldives", "Maldives", "Pristine white-sand beaches and crystal-clear waters with incredible marine life.", DestinationType.BEACH, 4.9, true, "https://images.unsplash.com/photo-1573843981267-be1999ff37cd?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1374&q=80"),
            createDestination("Grand Canyon", "United States", "One of the most spectacular natural wonders of the world.", DestinationType.NATURE, 4.8, true, "https://images.unsplash.com/photo-1575527048208-933f40c2f21e?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1366&q=80"),
            createDestination("Venice", "Italy", "A unique city built on water with historic canals, palaces and bridges.", DestinationType.CITY, 4.7, true, "https://images.unsplash.com/photo-1514890547357-a9ee288728e0?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1470&q=80")
        );
        
        destinationRepository.saveAll(destinations);
    }
    
    private Destination createDestination(String name, String country, String description, 
                                          DestinationType type, double averageRating, 
                                          boolean featured, String imageUrl) {
        Destination destination = new Destination();
        destination.setName(name);
        destination.setCountry(country);
        destination.setDescription(description);
        destination.setType(type);
        destination.setAverageRating(averageRating);
        destination.setFeatured(featured);
        destination.setImageUrl(imageUrl);
        return destination;
    }
    
    private void initTourPackages() {
        List<Destination> destinations = destinationRepository.findAll();
        
        // Create packages for each destination
        for (Destination destination : destinations) {
            createPackagesForDestination(destination);
        }
    }
    
    private void createPackagesForDestination(Destination destination) {
        // Create ADVENTURE package
        createPackage(
            "Adventure in " + destination.getName(),
            "Experience thrilling adventures in " + destination.getName() + " with guided tours, hiking, and outdoor activities.",
            7,
            new BigDecimal("1499.99"),
            destination,
            PackageType.ADVENTURE,
            25,
            LocalDate.now().plusMonths(1),
            LocalDate.now().plusMonths(6),
            true,
            15.0,
            "https://images.unsplash.com/photo-1527631746610-bca00a040d60?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1374&q=80"
        );
        
        // Create LUXURY package
        createPackage(
            "Luxury Retreat in " + destination.getName(),
            "Indulge in a luxurious vacation in " + destination.getName() + " with 5-star accommodations, fine dining, and exclusive experiences.",
            10,
            new BigDecimal("2999.99"),
            destination,
            PackageType.LUXURY,
            15,
            LocalDate.now().plusMonths(1),
            LocalDate.now().plusMonths(6),
            true,
            10.0,
            "https://images.unsplash.com/photo-1566073771259-6a8506099945?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1470&q=80"
        );
        
        // Create FAMILY package
        createPackage(
            "Family Fun in " + destination.getName(),
            "Create lasting memories with your family in " + destination.getName() + " with kid-friendly activities and comfortable accommodations.",
            7,
            new BigDecimal("1799.99"),
            destination,
            PackageType.FAMILY,
            20,
            LocalDate.now().plusMonths(1),
            LocalDate.now().plusMonths(6),
            false,
            12.0,
            "https://images.unsplash.com/photo-1581579438747-11f654ba2b79?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1470&q=80"
        );
        
        // Create CULTURAL package
        createPackage(
            "Cultural Experience in " + destination.getName(),
            "Immerse yourself in the rich culture of " + destination.getName() + " with guided tours to historical sites, museums, and local communities.",
            8,
            new BigDecimal("1599.99"),
            destination,
            PackageType.CULTURAL,
            18,
            LocalDate.now().plusMonths(1),
            LocalDate.now().plusMonths(6),
            false,
            8.0,
            "https://images.unsplash.com/photo-1488872890652-55aca0341408?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1374&q=80"
        );
        
        // Create BEACH package if destination is of type BEACH
        if (destination.getType() == DestinationType.BEACH) {
            createPackage(
                "Beach Getaway in " + destination.getName(),
                "Relax on the pristine beaches of " + destination.getName() + " with all-inclusive services and water activities.",
                7,
                new BigDecimal("1299.99"),
                destination,
                PackageType.BEACH,
                30,
                LocalDate.now().plusMonths(1),
                LocalDate.now().plusMonths(6),
                true,
                20.0,
                "https://images.unsplash.com/photo-1586375300052-37809c82be30?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1470&q=80"
            );
        }
        
        // Create HONEYMOON package
        createPackage(
            "Romantic Honeymoon in " + destination.getName(),
            "Celebrate your love with a romantic honeymoon in " + destination.getName() + " featuring private dinners, couple's massages, and scenic views.",
            9,
            new BigDecimal("2499.99"),
            destination,
            PackageType.HONEYMOON,
            2,
            LocalDate.now().plusMonths(1),
            LocalDate.now().plusMonths(6),
            true,
            15.0,
            "https://images.unsplash.com/photo-1518156677482-3bcf3d1d8301?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1470&q=80"
        );
    }
    
    private void createPackage(String name, String description, Integer duration, 
                              BigDecimal price, Destination destination, 
                              PackageType packageType, Integer maxGroupSize,
                              LocalDate startDate, LocalDate endDate, 
                              boolean featured, Double discountPercentage,
                              String imageUrl) {
        TourPackage tourPackage = new TourPackage();
        tourPackage.setName(name);
        tourPackage.setDescription(description);
        tourPackage.setDuration(duration);
        tourPackage.setPrice(price);
        tourPackage.setDestination(destination);
        tourPackage.setPackageType(packageType);
        tourPackage.setMaxGroupSize(maxGroupSize);
        tourPackage.setStartDate(startDate);
        tourPackage.setEndDate(endDate);
        tourPackage.setFeatured(featured);
        tourPackage.setDiscountPercentage(discountPercentage);
        tourPackage.setImageUrl(imageUrl);
        
        tourPackageRepository.save(tourPackage);
    }
} 