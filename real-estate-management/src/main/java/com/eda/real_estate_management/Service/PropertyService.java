package com.eda.real_estate_management.Service;

import java.util.List;
import java.util.UUID;

import com.eda.real_estate_management.DTO.CustomerDTO;
import com.eda.real_estate_management.DTO.EmployeeDTO;
import com.eda.real_estate_management.DTO.PropertyDTO;
import com.eda.real_estate_management.DTO.PropertyRequestDTO;

//CRUD işlemleri için interface
public interface PropertyService {

    List<PropertyDTO> getAllProperties();
    PropertyDTO getPropertyById(UUID propertyId);
    PropertyDTO createProperty(PropertyRequestDTO propertyDTO);
    PropertyDTO updateProperty(UUID propertyId, PropertyRequestDTO propertyDTO);
    void deleteProperty(UUID propertyId);
    List<CustomerDTO> getAllCustomersForForm();
    List<EmployeeDTO> getAllEmployeesForForm();
}
