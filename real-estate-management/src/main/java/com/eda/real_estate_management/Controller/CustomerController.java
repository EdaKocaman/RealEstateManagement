package com.eda.real_estate_management.Controller;

import java.util.Arrays;
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
import com.eda.real_estate_management.Entity.Customer.CustomerType;
import com.eda.real_estate_management.Service.CustomerService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;



@RestController //REST API isteklerini işlemek için denetleyici sınıfı
@RequestMapping("/api/customers") //Endpointlerin temel yolu
public class CustomerController {
    
    private final CustomerService customerService;
    
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Operation(summary = "Get all customers")
    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }  
    
    @Operation(summary = "Get customer by ID")
    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable UUID customerId) {
        return ResponseEntity.ok(customerService.getCustomerById(customerId));
    }

    @Operation(summary = "Create a new customer")
    @PostMapping
    public ResponseEntity<CustomerDTO> createCustomer(@Valid @RequestBody CustomerDTO customerDTO) {
        return ResponseEntity.ok(customerService.createCustomer(customerDTO));
    }

    @Operation(summary = "Update an existing customer")
    @PutMapping("/{customerId}")
    public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable UUID customerId, @RequestBody CustomerDTO customerDTO) {
        return ResponseEntity.ok(customerService.updateCustomer(customerId, customerDTO));
    }

    @Operation(summary = "Delete a customer by ID")
    @DeleteMapping("/{customerId}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable UUID customerId) {
        customerService.deleteCustomer(customerId);
        return ResponseEntity.noContent().build();
    }



    @GetMapping("/types")
    public ResponseEntity<List<String>> getCustomerTypes() {
        List<String> types = Arrays.stream(CustomerType.values())
                                   .map(Enum::name)
                                   .toList();
        return ResponseEntity.ok(types);
    }
}

