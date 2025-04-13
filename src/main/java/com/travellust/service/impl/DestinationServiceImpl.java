package com.travellust.service.impl;

import com.travellust.dto.DestinationDto;
import com.travellust.model.Destination;
import com.travellust.model.DestinationType;
import com.travellust.repository.DestinationRepository;
import com.travellust.service.DestinationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DestinationServiceImpl implements DestinationService {
    
    private final DestinationRepository destinationRepository;
    
    @Override
    public List<DestinationDto> getAllDestinations() {
        return destinationRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public DestinationDto getDestinationById(Long id) {
        return destinationRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new EntityNotFoundException("Destination not found with id: " + id));
    }
    
    @Override
    @Transactional
    public DestinationDto createDestination(DestinationDto destinationDto) {
        Destination destination = convertToEntity(destinationDto);
        Destination savedDestination = destinationRepository.save(destination);
        return convertToDto(savedDestination);
    }
    
    @Override
    @Transactional
    public DestinationDto updateDestination(Long id, DestinationDto destinationDto) {
        Destination destination = destinationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Destination not found with id: " + id));
        
        destination.setName(destinationDto.getName());
        destination.setCountry(destinationDto.getCountry());
        destination.setDescription(destinationDto.getDescription());
        destination.setImageUrl(destinationDto.getImageUrl());
        destination.setAverageRating(destinationDto.getAverageRating());
        destination.setType(destinationDto.getType());
        destination.setFeatured(destinationDto.isFeatured());
        
        Destination updatedDestination = destinationRepository.save(destination);
        return convertToDto(updatedDestination);
    }
    
    @Override
    @Transactional
    public void deleteDestination(Long id) {
        if (!destinationRepository.existsById(id)) {
            throw new EntityNotFoundException("Destination not found with id: " + id);
        }
        destinationRepository.deleteById(id);
    }
    
    @Override
    public List<DestinationDto> getFeaturedDestinations() {
        return destinationRepository.findByFeaturedTrue().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<DestinationDto> getDestinationsByType(DestinationType type) {
        return destinationRepository.findByType(type).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<DestinationDto> getDestinationsByCountry(String country) {
        return destinationRepository.findByCountry(country).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<String> getAllCountries() {
        return destinationRepository.findAllCountries();
    }
    
    @Override
    public List<DestinationDto> searchDestinations(String keyword) {
        return destinationRepository.findByNameContainingIgnoreCase(keyword).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<DestinationDto> getDestinationsByMinimumRating(Double minRating) {
        return destinationRepository.findByMinimumRating(minRating).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    private DestinationDto convertToDto(Destination destination) {
        return DestinationDto.builder()
                .id(destination.getId())
                .name(destination.getName())
                .country(destination.getCountry())
                .description(destination.getDescription())
                .imageUrl(destination.getImageUrl())
                .averageRating(destination.getAverageRating())
                .type(destination.getType())
                .featured(destination.isFeatured())
                .build();
    }
    
    private Destination convertToEntity(DestinationDto destinationDto) {
        Destination destination = new Destination();
        destination.setId(destinationDto.getId());
        destination.setName(destinationDto.getName());
        destination.setCountry(destinationDto.getCountry());
        destination.setDescription(destinationDto.getDescription());
        destination.setImageUrl(destinationDto.getImageUrl());
        destination.setAverageRating(destinationDto.getAverageRating());
        destination.setType(destinationDto.getType());
        destination.setFeatured(destinationDto.isFeatured());
        return destination;
    }
} 