package com.eda.real_estate_management.Service.Impl;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eda.real_estate_management.DTO.PropertyDTO;
import com.eda.real_estate_management.DTO.PropertySearchDTO;
import com.eda.real_estate_management.DTO.PropertySearchRequestDTO;
import com.eda.real_estate_management.Entity.Customer;
import com.eda.real_estate_management.Entity.Property;
import com.eda.real_estate_management.Entity.Property.PropertyStatus;
import com.eda.real_estate_management.Entity.PropertySearch;
import com.eda.real_estate_management.Exception.ResourceNotFoundException;
import com.eda.real_estate_management.Repository.CustomerRepository;
import com.eda.real_estate_management.Repository.PropertyRepository;
import com.eda.real_estate_management.Repository.PropertySearchRepository;
import com.eda.real_estate_management.Service.PropertySearchService;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class PropertySearchServiceImpl implements PropertySearchService {

    private final PropertySearchRepository propertySearchRepository;
    private final CustomerRepository customerRepository;
    private final PropertyRepository propertyRepository;

    public PropertySearchServiceImpl(PropertySearchRepository propertySearchRepository, 
                                        CustomerRepository customerRepository, 
                                            PropertyRepository propertyRepository) {
        this.propertySearchRepository = propertySearchRepository;
        this.customerRepository = customerRepository;
        this.propertyRepository = propertyRepository;
    }

    private PropertySearchDTO convertToDTO(PropertySearch search) {
        PropertySearchDTO dto = new PropertySearchDTO();
        dto.setSearchId(search.getSearchId());
        dto.setCustomerId(search.getCustomer().getCustomerId());
        dto.setPropertyType(search.getPropertyType());
        dto.setMinPrice(search.getMinPrice());
        dto.setMaxPrice(search.getMaxPrice());
        dto.setMinSquareMeters(search.getMinSquareMeters());
        dto.setMaxSquareMeters(search.getMaxSquareMeters());
        dto.setRoomCount(search.getRoomCount());
        dto.setHeatingType(search.getHeatingType());
        dto.setStatus(search.getStatus() != null ? search.getStatus().name() : null); // <- ekledik
        return dto;
    }


    private PropertySearch convertToEntity(PropertySearchRequestDTO dto, PropertySearch entity) {
        Customer customer = customerRepository.findById(dto.getCustomerId())
            .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + dto.getCustomerId()));
        entity.setCustomer(customer);
        entity.setPropertyType(dto.getPropertyType());
        entity.setMinPrice(dto.getMinPrice());
        entity.setMaxPrice(dto.getMaxPrice());
        entity.setMinSquareMeters(dto.getMinSquareMeters());
        entity.setMaxSquareMeters(dto.getMaxSquareMeters());
        entity.setRoomCount(dto.getRoomCount());
        entity.setHeatingType(dto.getHeatingType());

        if (dto.getStatus() != null && !dto.getStatus().isEmpty()) {
            entity.setStatus(PropertyStatus.valueOf(dto.getStatus())); // <- ekledik
        }
        return entity;
    }


    private PropertyDTO convertPropertyToDTO(Property property) {
        PropertyDTO dto = new PropertyDTO();
        dto.setId(property.getPropertyId());
        dto.setTitle(property.getTitle());
        dto.setDescription(property.getDescription());
        dto.setAddress(property.getAddress());
        dto.setPrice(property.getPrice());
        dto.setPropertyType(property.getPropertyType());
        dto.setStatus(property.getStatus() != null ? property.getStatus().name() : null);
        dto.setRoomCount(property.getRoomCount());
        dto.setSquareMeters(property.getSquareMeters());
        dto.setFloorNumber(property.getFloorNumber());
        dto.setHeatingType(property.getHeatingType());
        dto.setCustomerId(property.getCustomer().getCustomerId());
        dto.setEmployeeId(property.getEmployee() != null ? property.getEmployee().getEmployeeId() : null);

        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PropertySearchDTO> getAllSearches() {
        return propertySearchRepository.findAll()
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PropertySearchDTO getSearchById(UUID searchId) {
        PropertySearch search = propertySearchRepository.findById(searchId)
            .orElseThrow(() -> new ResourceNotFoundException("Property search not found with id: " + searchId));
        return convertToDTO(search);
    }

    @Override
    @Transactional
    public PropertySearchDTO createSearch(PropertySearchRequestDTO dto) {
        PropertySearch search = new PropertySearch();
        convertToEntity(dto, search);
        PropertySearch saved = propertySearchRepository.save(search);
        return convertToDTO(saved);
    }

    @Override
    @Transactional
    public PropertySearchDTO updateSearch(UUID searchId, PropertySearchRequestDTO dto) {
        PropertySearch search = propertySearchRepository.findById(searchId)
            .orElseThrow(() -> new ResourceNotFoundException("Property search not found with id: " + searchId));
        convertToEntity(dto, search);
        PropertySearch updated = propertySearchRepository.save(search);
        return convertToDTO(updated);
    }

    @Override
    @Transactional
    public void deleteSearch(UUID searchId) {
        PropertySearch search = propertySearchRepository.findById(searchId)
            .orElseThrow(() -> new ResourceNotFoundException("Property search not found with id: " + searchId));
        propertySearchRepository.delete(search);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PropertyDTO> searchProperties(PropertySearchRequestDTO dto) {
        Specification<Property> spec = (Root<Property> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (dto.getCustomerId() != null) {
                predicates.add(cb.equal(root.get("customer").get("customerId"), dto.getCustomerId()));
            }
            
            // Property Type filtresi
            if (dto.getPropertyType() != null && !dto.getPropertyType().isEmpty()) {
                predicates.add(cb.equal(cb.lower(root.get("propertyType")), dto.getPropertyType().toLowerCase()));
            }
            
            // Min fiyat filtresi
            if (dto.getMinPrice() != null && dto.getMinPrice().compareTo(BigDecimal.ZERO) > 0) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("price"), dto.getMinPrice()));
            }
            // Max fiyat filtresi
            if (dto.getMaxPrice() != null && dto.getMaxPrice().compareTo(BigDecimal.ZERO) > 0) {
                predicates.add(cb.lessThanOrEqualTo(root.get("price"), dto.getMaxPrice()));
            }

            // Min Metrekare filtresi
            if (dto.getMinSquareMeters() != null && dto.getMinSquareMeters() > 0) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("squareMeters"), dto.getMinSquareMeters()));
            }
            
            // Max Metrekare filtresi
            if (dto.getMaxSquareMeters() != null && dto.getMaxSquareMeters() > 0) {
                predicates.add(cb.lessThanOrEqualTo(root.get("squareMeters"), dto.getMaxSquareMeters()));
            }
            if (dto.getRoomCount() != null && !dto.getRoomCount().isEmpty()) {
                predicates.add(cb.equal(root.get("roomCount"), dto.getRoomCount()));
            }
            if (dto.getHeatingType() != null && !dto.getHeatingType().isEmpty()) {
                predicates.add(cb.equal(cb.lower(root.get("heatingType")), dto.getHeatingType().toLowerCase()));
            }

            // Satış tipi filtresi (FOR_SALE, FOR_RENT, SOLD, RENTED)
            if (dto.getStatus() != null && !dto.getStatus().isEmpty()) {
                predicates.add(cb.equal(root.get("status"), PropertyStatus.valueOf(dto.getStatus())));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return propertyRepository.findAll(spec)
                .stream()
                .map(this::convertPropertyToDTO)
                .collect(Collectors.toList());
    }

}
