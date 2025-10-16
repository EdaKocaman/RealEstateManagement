package com.eda.real_estate_management.Controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eda.real_estate_management.DTO.CustomerDTO;
import com.eda.real_estate_management.DTO.EmployeeDTO;
import com.eda.real_estate_management.DTO.PropertyDTO;
import com.eda.real_estate_management.DTO.PropertyRequestDTO;
import com.eda.real_estate_management.Entity.Property.PropertyStatus;
import com.eda.real_estate_management.Service.PropertyService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/properties")
public class PropertyController {
    
    private final PropertyService propertyService;

    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }


    @Operation(summary = "Get all properties", description = "Returns a list of all properties")
    @GetMapping
    public ResponseEntity<List<PropertyDTO>> getAllProperties() {
        return ResponseEntity.ok(propertyService.getAllProperties());
    }


    @Operation(summary = "Get property by ID", description = "Returns a property by its ID")
    @GetMapping("/{propertyId}")
    public ResponseEntity<PropertyDTO> getPropertyById(@PathVariable UUID propertyId) {
        return ResponseEntity.ok(propertyService.getPropertyById(propertyId));
    }

    @Operation(summary = "Create new property", description = "Adds a new property to the system")
    @PostMapping
    public ResponseEntity<PropertyDTO> createProperty(@Valid @RequestBody PropertyRequestDTO dto) {
        return ResponseEntity.ok(propertyService.createProperty(dto));
    }

    
    @Operation(summary = "Update property", description = "Updates property information by ID")
    @PutMapping("/{propertyId}")
    public ResponseEntity<PropertyDTO> updateProperty(@PathVariable UUID propertyId, @RequestBody PropertyRequestDTO dto) {
        return ResponseEntity.ok(propertyService.updateProperty(propertyId, dto));
    }

    
    @Operation(summary = "Delete property", description = "Deletes a property by ID")
    @DeleteMapping("/{propertyId}")
    public ResponseEntity<Void> deleteProperty(@PathVariable UUID propertyId) {
        propertyService.deleteProperty(propertyId);
        return ResponseEntity.noContent().build();
    }
    
    @Operation(summary = "Get property status list", description = "Returns all possible property statuses")
    @GetMapping("/statuses")
    public ResponseEntity<PropertyStatus[]> getPropertyStatuses() {
        return ResponseEntity.ok(PropertyStatus.values());
    }

    // Customer dropdown için endpoint
    @Operation(summary = "Get all customers", description = "Returns a list of all customers (for property form)")
    @GetMapping("/customers")
    public ResponseEntity<List<CustomerDTO>> getAllCustomersForForm() {
        return ResponseEntity.ok(propertyService.getAllCustomersForForm());
    }

    // Employee dropdown için endpoint
    @Operation(summary = "Get all employees", description = "Returns a list of all employees (for property form)")
    @GetMapping("/employees")
    public ResponseEntity<List<EmployeeDTO>> getAllEmployeesForForm() {
        return ResponseEntity.ok(propertyService.getAllEmployeesForForm());
    }


}
