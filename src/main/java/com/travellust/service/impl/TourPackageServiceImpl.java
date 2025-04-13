package com.travellust.service.impl;

import com.travellust.dto.TourPackageDto;
import com.travellust.model.Destination;
import com.travellust.model.PackageType;
import com.travellust.model.TourPackage;
import com.travellust.repository.DestinationRepository;
import com.travellust.repository.TourPackageRepository;
import com.travellust.service.TourPackageService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TourPackageServiceImpl implements TourPackageService {
    
    private final TourPackageRepository tourPackageRepository;
    private final DestinationRepository destinationRepository;
    
    @Override
    public List<TourPackageDto> getAllTourPackages() {
        return tourPackageRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public TourPackageDto getTourPackageById(Long id) {
        return tourPackageRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new EntityNotFoundException("Tour package not found with id: " + id));
    }
    
    @Override
    @Transactional
    public TourPackageDto createTourPackage(TourPackageDto tourPackageDto) {
        TourPackage tourPackage = convertToEntity(tourPackageDto);
        TourPackage savedTourPackage = tourPackageRepository.save(tourPackage);
        return convertToDto(savedTourPackage);
    }
    
    @Override
    @Transactional
    public TourPackageDto updateTourPackage(Long id, TourPackageDto tourPackageDto) {
        TourPackage tourPackage = tourPackageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tour package not found with id: " + id));
        
        updateTourPackageFromDto(tourPackage, tourPackageDto);
        
        TourPackage updatedTourPackage = tourPackageRepository.save(tourPackage);
        return convertToDto(updatedTourPackage);
    }
    
    @Override
    @Transactional
    public void deleteTourPackage(Long id) {
        if (!tourPackageRepository.existsById(id)) {
            throw new EntityNotFoundException("Tour package not found with id: " + id);
        }
        tourPackageRepository.deleteById(id);
    }
    
    @Override
    public List<TourPackageDto> getFeaturedTourPackages() {
        return tourPackageRepository.findByFeaturedTrue().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<TourPackageDto> getTourPackagesByDestination(Long destinationId) {
        return tourPackageRepository.findByDestinationId(destinationId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<TourPackageDto> getPackagesByType(PackageType packageType) {
        return tourPackageRepository.findByPackageType(packageType).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<TourPackageDto> getPackagesByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return tourPackageRepository.findByPriceRange(minPrice, maxPrice).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<TourPackageDto> getPackagesByDateRange(LocalDate startDate, LocalDate endDate) {
        return tourPackageRepository.findByDateRange(startDate, endDate).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<TourPackageDto> getPackagesByMaxDuration(Integer maxDuration) {
        return tourPackageRepository.findByMaxDuration(maxDuration).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<TourPackageDto> getPackagesWithDiscount() {
        return tourPackageRepository.findWithDiscount().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<TourPackageDto> getPackagesByDestinationCountryAndType(String country, PackageType packageType) {
        return tourPackageRepository.findByDestination_CountryAndPackageType(country, packageType).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<TourPackageDto> searchTourPackages(String searchTerm) {
        // In a real implementation, you might use a full-text search or a more sophisticated query
        String term = searchTerm.toLowerCase();
        return tourPackageRepository.findAll().stream()
                .filter(pkg -> 
                    pkg.getName().toLowerCase().contains(term) || 
                    pkg.getDescription().toLowerCase().contains(term) ||
                    (pkg.getDestination() != null && pkg.getDestination().getName().toLowerCase().contains(term)) ||
                    (pkg.getDestination() != null && pkg.getDestination().getCountry().toLowerCase().contains(term))
                )
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    private TourPackageDto convertToDto(TourPackage tourPackage) {
        Double price = tourPackage.getPrice() != null ? tourPackage.getPrice().doubleValue() : null;
        Double discountPercentage = tourPackage.getDiscountPercentage() != null ? 
                tourPackage.getDiscountPercentage().doubleValue() : null;
        Double discountedPrice = tourPackage.getDiscountedPrice() != null ? 
                tourPackage.getDiscountedPrice().doubleValue() : null;
                
        return TourPackageDto.builder()
                .id(tourPackage.getId())
                .name(tourPackage.getName())
                .description(tourPackage.getDescription())
                .duration(tourPackage.getDuration())
                .price(price)
                .imageUrl(tourPackage.getImageUrl())
                .maxGroupSize(tourPackage.getMaxGroupSize())
                .startDate(tourPackage.getStartDate())
                .endDate(tourPackage.getEndDate())
                .packageType(tourPackage.getPackageType())
                .featured(tourPackage.isFeatured())
                .discountPercentage(discountPercentage)
                .discountedPrice(discountedPrice)
                .destinationId(tourPackage.getDestination() != null ? tourPackage.getDestination().getId() : null)
                .destinationName(tourPackage.getDestination() != null ? tourPackage.getDestination().getName() : null)
                .destinationCountry(tourPackage.getDestination() != null ? tourPackage.getDestination().getCountry() : null)
                .build();
    }
    
    private TourPackage convertToEntity(TourPackageDto dto) {
        TourPackage tourPackage = new TourPackage();
        
        if (dto.getId() != null) {
            tourPackage.setId(dto.getId());
        }
        
        tourPackage.setName(dto.getName());
        tourPackage.setDescription(dto.getDescription());
        tourPackage.setDuration(dto.getDuration());
        tourPackage.setPrice(dto.getPrice() != null ? BigDecimal.valueOf(dto.getPrice()) : null);
        tourPackage.setImageUrl(dto.getImageUrl());
        tourPackage.setMaxGroupSize(dto.getMaxGroupSize());
        tourPackage.setStartDate(dto.getStartDate());
        tourPackage.setEndDate(dto.getEndDate());
        tourPackage.setPackageType(dto.getPackageType());
        tourPackage.setFeatured(dto.isFeatured());
        tourPackage.setDiscountPercentage(dto.getDiscountPercentage() != null ? 
                Double.valueOf(dto.getDiscountPercentage()) : null);
        
        if (dto.getDestinationId() != null) {
            Destination destination = destinationRepository.findById(dto.getDestinationId())
                    .orElseThrow(() -> new EntityNotFoundException("Destination not found with id: " + dto.getDestinationId()));
            tourPackage.setDestination(destination);
        }
        
        return tourPackage;
    }
    
    private void updateTourPackageFromDto(TourPackage tourPackage, TourPackageDto dto) {
        tourPackage.setName(dto.getName());
        tourPackage.setDescription(dto.getDescription());
        tourPackage.setDuration(dto.getDuration());
        tourPackage.setPrice(dto.getPrice() != null ? BigDecimal.valueOf(dto.getPrice()) : null);
        tourPackage.setImageUrl(dto.getImageUrl());
        tourPackage.setMaxGroupSize(dto.getMaxGroupSize());
        tourPackage.setStartDate(dto.getStartDate());
        tourPackage.setEndDate(dto.getEndDate());
        tourPackage.setPackageType(dto.getPackageType());
        tourPackage.setFeatured(dto.isFeatured());
        tourPackage.setDiscountPercentage(dto.getDiscountPercentage() != null ? 
                Double.valueOf(dto.getDiscountPercentage()) : null);
        
        if (dto.getDestinationId() != null) {
            Destination destination = destinationRepository.findById(dto.getDestinationId())
                    .orElseThrow(() -> new EntityNotFoundException("Destination not found with id: " + dto.getDestinationId()));
            tourPackage.setDestination(destination);
        }
    }
    
    // Helper methods for type conversion
    private Double convertBigDecimalToDouble(BigDecimal value) {
        return value != null ? value.doubleValue() : null;
    }
    
    private BigDecimal convertDoubleToBigDecimal(Double value) {
        return value != null ? BigDecimal.valueOf(value) : null;
    }
} 