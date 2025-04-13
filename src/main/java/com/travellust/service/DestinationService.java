package com.travellust.service;

import com.travellust.dto.DestinationDto;
import com.travellust.model.DestinationType;

import java.util.List;

public interface DestinationService {
    
    List<DestinationDto> getAllDestinations();
    
    DestinationDto getDestinationById(Long id);
    
    DestinationDto createDestination(DestinationDto destinationDto);
    
    DestinationDto updateDestination(Long id, DestinationDto destinationDto);
    
    void deleteDestination(Long id);
    
    List<DestinationDto> getFeaturedDestinations();
    
    List<DestinationDto> getDestinationsByType(DestinationType type);
    
    List<DestinationDto> getDestinationsByCountry(String country);
    
    List<String> getAllCountries();
    
    List<DestinationDto> searchDestinations(String keyword);
    
    List<DestinationDto> getDestinationsByMinimumRating(Double minRating);
} 