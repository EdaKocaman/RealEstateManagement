package com.eda.real_estate_management.Service.Impl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eda.real_estate_management.DTO.CustomerDTO;
import com.eda.real_estate_management.Entity.Customer;
import com.eda.real_estate_management.Exception.ResourceNotFoundException;
import com.eda.real_estate_management.Repository.CustomerRepository;
import com.eda.real_estate_management.Service.CustomerService;

// Service implementasyonu: CustomerService interface'indeki metodları doldurur
@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    // Constructor ile repository enjekte edilir
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    private CustomerDTO convertToDTO(Customer customer) {
        CustomerDTO dto = new CustomerDTO();
        dto.setId(customer.getCustomerId());
        dto.setFirstName(customer.getFirstName());
        dto.setLastName(customer.getLastName());
        dto.setCustomerType(customer.getCustomerType().name());
        dto.setPhone(customer.getPhone());
        return dto;
    }

    private Customer convertToEntity(CustomerDTO dto) {
        Customer customer = new Customer();
        customer.setFirstName(dto.getFirstName());
        customer.setLastName(dto.getLastName());
        if (dto.getCustomerType() != null) {
            customer.setCustomerType(Customer.CustomerType.valueOf(dto.getCustomerType()));
        }
        customer.setPhone(dto.getPhone());
        return customer;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerDTO getCustomerById(UUID customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));
        return convertToDTO(customer);
    }

    @Override
    @Transactional
    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        Customer customer = convertToEntity(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);
        return convertToDTO(savedCustomer);
    }

    @Override
    @Transactional
    public CustomerDTO updateCustomer(UUID customerId, CustomerDTO customerDTO) {
        Customer existingCustomer = customerRepository.findById(customerId)
            .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));

        existingCustomer.setFirstName(customerDTO.getFirstName());
        existingCustomer.setLastName(customerDTO.getLastName());
        if (customerDTO.getCustomerType() != null) {
            existingCustomer.setCustomerType(Customer.CustomerType.valueOf(customerDTO.getCustomerType()));
        }
        existingCustomer.setPhone(customerDTO.getPhone());

        Customer updatedCustomer = customerRepository.save(existingCustomer);
        return convertToDTO(updatedCustomer);
    }

    @Override
    @Transactional
    public void deleteCustomer(UUID customerId) {
        Customer existingCustomer = customerRepository.findById(customerId)
            .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));
        customerRepository.delete(existingCustomer);
    }
    
}
