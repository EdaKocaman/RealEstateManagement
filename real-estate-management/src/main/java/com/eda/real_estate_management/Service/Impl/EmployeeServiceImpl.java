package com.eda.real_estate_management.Service.Impl;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eda.real_estate_management.DTO.EmployeeDTO;
import com.eda.real_estate_management.DTO.EmployeeRequestDTO;
import com.eda.real_estate_management.Entity.Company;
import com.eda.real_estate_management.Entity.Employee;
import com.eda.real_estate_management.Exception.ResourceNotFoundException;
import com.eda.real_estate_management.Repository.CompanyRepository;
import com.eda.real_estate_management.Repository.EmployeeRepository;
import com.eda.real_estate_management.Service.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final CompanyRepository companyRepository;


    // Constructor ile repository enjekte edilir
    public EmployeeServiceImpl(EmployeeRepository employeeRepository, CompanyRepository companyRepository) {
        this.employeeRepository = employeeRepository;
        this.companyRepository = companyRepository;
    }

    // Entity -> DTO
    private EmployeeDTO convertToDTO(Employee employee) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(employee.getEmployeeId());
        dto.setFirstName(employee.getFirstName());
        dto.setLastName(employee.getLastName());
        dto.setPhone(employee.getPhone());
        dto.setEmployeeRole(employee.getEmployeeRole().name());
        dto.setCompanyId(employee.getCompany() != null ? employee.getCompany().getCompanyId() : null);
        
        return dto;
    }

    // RequestDTO -> Entity
    private Employee convertToEntity(EmployeeRequestDTO requestDTO) {
        Employee employee = new Employee();
        employee.setFirstName(requestDTO.getFirstName());
        employee.setLastName(requestDTO.getLastName());
        employee.setPhone(requestDTO.getPhone());
        employee.setEmployeeRole(Employee.EmployeeRole.valueOf(requestDTO.getEmployeeRole()));
        
        Company company = companyRepository.findById(requestDTO.getCompanyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + requestDTO.getCompanyId()));
        employee.setCompany(company);

        return employee;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDTO> getAllEmployees() {
        return employeeRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeDTO getEmployeeById(UUID employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + employeeId));
        return convertToDTO(employee);
    }

    @Override
    @Transactional
    public EmployeeDTO createEmployee(EmployeeRequestDTO employeeRequestDTO) {
        Employee employee = convertToEntity(employeeRequestDTO);
        Employee savedEmployee = employeeRepository.save(employee);
        return convertToDTO(savedEmployee);
    }


    @Override
    @Transactional
    public EmployeeDTO updateEmployee(UUID employeeId, EmployeeRequestDTO employeeRequestDTO) {
        Employee existingEmployee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + employeeId));

        existingEmployee.setFirstName(employeeRequestDTO.getFirstName());
        existingEmployee.setLastName(employeeRequestDTO.getLastName());
        existingEmployee.setEmployeeRole(Employee.EmployeeRole.valueOf(employeeRequestDTO.getEmployeeRole()));

        Company company = companyRepository.findById(employeeRequestDTO.getCompanyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + employeeRequestDTO.getCompanyId()));
        existingEmployee.setCompany(company);

        Employee updatedEmployee = employeeRepository.save(existingEmployee);
        return convertToDTO(updatedEmployee);
    }

    @Override
    @Transactional
    public void deleteEmployee(UUID employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + employeeId));
        employeeRepository.delete(employee);
    }
    
}
