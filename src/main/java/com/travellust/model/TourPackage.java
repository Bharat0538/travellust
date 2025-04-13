package com.travellust.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tour_packages")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TourPackage {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(length = 1000)
    private String description;
    
    @Column(nullable = false)
    private Integer duration; // in days
    
    @Column(nullable = false)
    private BigDecimal price;
    
    private String imageUrl;
    
    private Integer maxGroupSize;
    
    private LocalDate startDate;
    
    private LocalDate endDate;
    
    @Enumerated(EnumType.STRING)
    private PackageType packageType;
    
    @Column(name = "featured")
    private boolean featured = false;
    
    private Double discountPercentage = 0.0;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_id")
    private Destination destination;
    
    @OneToMany(mappedBy = "tourPackage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Booking> bookings = new ArrayList<>();
    
    public BigDecimal getDiscountedPrice() {
        if (discountPercentage == null || discountPercentage == 0) {
            return price;
        }
        return price.subtract(price.multiply(BigDecimal.valueOf(discountPercentage / 100)));
    }
} 