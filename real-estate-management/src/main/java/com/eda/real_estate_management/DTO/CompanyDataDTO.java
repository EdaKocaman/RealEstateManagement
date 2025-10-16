package com.eda.real_estate_management.DTO;

import java.util.List;

public class CompanyDataDTO {
    
    private CompanyDTO company;
    private List<EmployeeDTO> employees;
    private List<CustomerDTO> customers;
    private List<PropertyDTO> properties;

    public CompanyDataDTO(CompanyDTO company, List<EmployeeDTO> employees, List<CustomerDTO> customers, List<PropertyDTO> properties) {
        this.company = company;
        this.employees = employees;
        this.customers = customers;
        this.properties = properties;
    }

    public CompanyDataDTO() {}

    public CompanyDTO getCompany() { return company; }
    public void setCompany(CompanyDTO company) { this.company = company; }

    public List<EmployeeDTO> getEmployees() { return employees; }
    public void setEmployees(List<EmployeeDTO> employees) { this.employees = employees; }

    public List<CustomerDTO> getCustomers() { return customers; }
    public void setCustomers(List<CustomerDTO> customers) { this.customers = customers; }

    public List<PropertyDTO> getProperties() { return properties; }
    public void setProperties(List<PropertyDTO> properties) { this.properties = properties; }
}
