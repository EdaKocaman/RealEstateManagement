package com.eda.real_estate_management.Service;

import java.util.List;
import java.util.UUID;

import com.eda.real_estate_management.DTO.EmployeeDTO;
import com.eda.real_estate_management.DTO.EmployeeRequestDTO;

// Employee ile ilgili servis metodlarını tanımlar
public interface EmployeeService {

    List<EmployeeDTO> getAllEmployees();
    EmployeeDTO getEmployeeById(UUID employeeId);
    EmployeeDTO createEmployee(EmployeeRequestDTO employeeRequestDTO);
    EmployeeDTO updateEmployee(UUID employeeId, EmployeeRequestDTO employeeRequestDTO);
    void deleteEmployee(UUID employeeId);
}
