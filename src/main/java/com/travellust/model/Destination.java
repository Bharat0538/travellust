package com.travellust.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "destinations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Destination {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private String country;
    
    @Column(length = 1000)
    private String description;
    
    private String imageUrl;
    
    @Column(name = "avg_rating")
    private Double averageRating;
    
    @Enumerated(EnumType.STRING)
    private DestinationType type;
    
    @Column(name = "featured")
    private boolean featured = false;
    
    @OneToMany(mappedBy = "destination", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TourPackage> tourPackages = new ArrayList<>();
} 