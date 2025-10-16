package com.eda.real_estate_management.Service;

import java.util.List;
import java.util.UUID;

import com.eda.real_estate_management.DTO.PropertyDTO;
import com.eda.real_estate_management.DTO.PropertySearchDTO;
import com.eda.real_estate_management.DTO.PropertySearchRequestDTO;

//CRUD işlemleri için interface
public interface PropertySearchService {

    List<PropertySearchDTO> getAllSearches();
    PropertySearchDTO getSearchById(UUID searchId);
    PropertySearchDTO createSearch(PropertySearchRequestDTO dto);
    PropertySearchDTO updateSearch(UUID searchId, PropertySearchRequestDTO dto);
    void deleteSearch(UUID searchId);
    List<PropertyDTO> searchProperties(PropertySearchRequestDTO dto);   
}
