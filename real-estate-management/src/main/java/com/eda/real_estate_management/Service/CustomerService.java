package com.eda.real_estate_management.Service;

import java.util.List;
import java.util.UUID;

import com.eda.real_estate_management.DTO.CustomerDTO;


// Service interface: Customer ile ilgili işlemleri tanımlar
public interface CustomerService {
    
    List<CustomerDTO> getAllCustomers();
    CustomerDTO getCustomerById(UUID customerId);
    CustomerDTO createCustomer(CustomerDTO customerDTO);
    CustomerDTO updateCustomer(UUID customerId, CustomerDTO customerDTO);
    void deleteCustomer(UUID customerId);

}
