package com.eda.real_estate_management.Service.Impl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eda.real_estate_management.DTO.CustomerDTO;
import com.eda.real_estate_management.DTO.EmployeeDTO;
import com.eda.real_estate_management.DTO.PropertyDTO;
import com.eda.real_estate_management.DTO.PropertyRequestDTO;
import com.eda.real_estate_management.Entity.Customer;
import com.eda.real_estate_management.Entity.Employee;
import com.eda.real_estate_management.Entity.Property;
import com.eda.real_estate_management.Exception.ResourceNotFoundException;
import com.eda.real_estate_management.Repository.CustomerRepository;
import com.eda.real_estate_management.Repository.EmployeeRepository;
import com.eda.real_estate_management.Repository.PropertyRepository;
import com.eda.real_estate_management.Service.PropertyService;


// Service implementasyonu: PropertyService interface'indeki metodları doldurur
@Service
public class PropertyServiceImpl implements PropertyService {

    private final PropertyRepository propertyRepository;
    private final CustomerRepository customerRepository;
    private final EmployeeRepository employeeRepository;

    public PropertyServiceImpl(PropertyRepository propertyRepository, CustomerRepository customerRepository, EmployeeRepository employeeRepository) {
        this.propertyRepository = propertyRepository;
        this.customerRepository = customerRepository;
        this.employeeRepository = employeeRepository;
    }

    private PropertyDTO convertToDTO(Property property) {
        PropertyDTO dto = new PropertyDTO();
        dto.setId(property.getPropertyId());
        dto.setTitle(property.getTitle());
        dto.setDescription(property.getDescription());
        dto.setAddress(property.getAddress());
        dto.setPrice(property.getPrice());
        dto.setPropertyType(property.getPropertyType());
        dto.setStatus(property.getStatus() != null ? property.getStatus().name() : null); // Enum → String
        dto.setRoomCount(property.getRoomCount());
        dto.setSquareMeters(property.getSquareMeters());
        dto.setFloorNumber(property.getFloorNumber());
        dto.setHeatingType(property.getHeatingType());
        dto.setCustomerId(property.getCustomer().getCustomerId());
        dto.setEmployeeId(property.getEmployee() != null ? property.getEmployee().getEmployeeId() : null); 
        return dto;
    }

    private Property convertToEntity(PropertyRequestDTO dto, Property property) {
        property.setTitle(dto.getTitle());
        property.setDescription(dto.getDescription());
        property.setAddress(dto.getAddress());
        property.setPrice(dto.getPrice());
        property.setPropertyType(dto.getPropertyType());
        property.setStatus(dto.getStatus() != null ? Property.PropertyStatus.valueOf(dto.getStatus()) : null); // String → Enum
        property.setHeatingType(dto.getHeatingType());
        property.setFloorNumber(dto.getFloorNumber() != null ? dto.getFloorNumber() : 0); // null check
        property.setRoomCount(dto.getRoomCount());
        property.setSquareMeters(dto.getSquareMeters() != null ? dto.getSquareMeters() : 0); // null check

    Customer customer = customerRepository.findById(dto.getCustomerId())
        .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + dto.getCustomerId()));
    property.setCustomer(customer);

    Employee employee = employeeRepository.findById(dto.getEmployeeId())
        .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + dto.getEmployeeId()));
    property.setEmployee(employee);

        return property;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PropertyDTO> getAllProperties() {
        return propertyRepository.findAll()
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    
    @Override
    @Transactional(readOnly = true)
    public PropertyDTO getPropertyById(UUID propertyId) {
        Property property = propertyRepository.findById(propertyId)
            .orElseThrow(() -> new ResourceNotFoundException("Property not found with id: " + propertyId));
        return convertToDTO(property);
    }

    @Override
    @Transactional
    public PropertyDTO createProperty(PropertyRequestDTO dto) {

        // Müşteri bulma
        Customer customer;
        if (dto.getCustomerId() != null) {
            // UUID varsa direkt bul
            customer = customerRepository.findById(dto.getCustomerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + dto.getCustomerId()));
        } else if (dto.getCustomerFirstName() != null && dto.getCustomerLastName() != null) {
            // UUID yoksa isim + soyisim üzerinden bul
            customer = customerRepository.findByFirstNameAndLastName(dto.getCustomerFirstName(), dto.getCustomerLastName())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Customer not found with name: " + dto.getCustomerFirstName() + " " + dto.getCustomerLastName()));
        } else {
            throw new IllegalArgumentException("Customer information is required");
        }

        // Çalışan bulma
        Employee employee;
        if (dto.getEmployeeId() != null) {
            employee = employeeRepository.findById(dto.getEmployeeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + dto.getEmployeeId()));
        } else if (dto.getEmployeeFirstName() != null && dto.getEmployeeLastName() != null) {
            employee = employeeRepository.findByFirstNameAndLastName(dto.getEmployeeFirstName(), dto.getEmployeeLastName())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Employee not found with name: " + dto.getEmployeeFirstName() + " " + dto.getEmployeeLastName()));
        } else {
            throw new IllegalArgumentException("Employee information is required");
        }

        Property property = new Property();
        property.setTitle(dto.getTitle());
        property.setDescription(dto.getDescription());
        property.setAddress(dto.getAddress());
        property.setPrice(dto.getPrice());
        property.setPropertyType(dto.getPropertyType());
        property.setStatus(dto.getStatus() != null ? Property.PropertyStatus.valueOf(dto.getStatus()) : null);
        property.setHeatingType(dto.getHeatingType());
        property.setFloorNumber(dto.getFloorNumber() != null ? dto.getFloorNumber() : 0);
        property.setRoomCount(dto.getRoomCount());
        property.setSquareMeters(dto.getSquareMeters() != null ? dto.getSquareMeters() : 0);

        property.setCustomer(customer);
        property.setEmployee(employee);

        Property saved = propertyRepository.save(property);
        return convertToDTO(saved);
    }



    @Override
    @Transactional
    public PropertyDTO updateProperty(UUID propertyId, PropertyRequestDTO dto) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with id: " + propertyId));
        convertToEntity(dto, property);
        Property updated = propertyRepository.save(property);
        return convertToDTO(updated);
    }

    @Override
    @Transactional
    public void deleteProperty(UUID propertyId) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with id: " + propertyId));
        propertyRepository.delete(property);
    }


    @Override
    @Transactional(readOnly = true)
    public List<CustomerDTO> getAllCustomersForForm() {
        return customerRepository.findAll()
                .stream()
                .map(customer -> {
                    CustomerDTO dto = new CustomerDTO();
                    dto.setId(customer.getCustomerId());
                    dto.setFirstName(customer.getFirstName());
                    dto.setLastName(customer.getLastName());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDTO> getAllEmployeesForForm() {
        return employeeRepository.findAll()
                .stream()
                .map(employee -> {
                    EmployeeDTO dto = new EmployeeDTO();
                    dto.setId(employee.getEmployeeId());
                    dto.setFirstName(employee.getFirstName());
                    dto.setLastName(employee.getLastName());
                    return dto;
                })
                .collect(Collectors.toList());
    }

}