package com.travellust.dto;

import com.travellust.model.DestinationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DestinationDto {
    private Long id;
    private String name;
    private String country;
    private String description;
    private String imageUrl;
    private Double averageRating;
    private DestinationType type;
    private boolean featured;
} 