package com.eda.real_estate_management.Service.Impl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eda.real_estate_management.DTO.CompanyDTO;
import com.eda.real_estate_management.DTO.CustomerDTO;
import com.eda.real_estate_management.DTO.EmployeeDTO;
import com.eda.real_estate_management.DTO.PropertyDTO;
import com.eda.real_estate_management.Entity.Company;
import com.eda.real_estate_management.Entity.Customer;
import com.eda.real_estate_management.Entity.Employee;
import com.eda.real_estate_management.Entity.Property;
import com.eda.real_estate_management.Exception.ResourceNotFoundException;
import com.eda.real_estate_management.Repository.CompanyRepository;
import com.eda.real_estate_management.Service.CompanyService;
/**
 * CompanyServiceImpl, CompanyService arayüzünde tanımlanan metotların gerçek işleyişini içerir.
 * Bu katman controller ile repository arasında bir köprü görevi görür.
 */


@Service
public class CompanyServiceImpl implements CompanyService{
    
    private final CompanyRepository companyRepository;
    
    
    /**
     * Constructor injection kullanarak repository bağımlılığını sağlar.
     *  test edilebilirlik açısından daha avantajlıdır.
     */
    public CompanyServiceImpl(CompanyRepository companyRepository ) {
        this.companyRepository = companyRepository;
        
    }

    private CompanyDTO convertToDTO(Company company) {
        CompanyDTO dto = new CompanyDTO();
        dto.setId(company.getCompanyId());
        dto.setName(company.getName());
        dto.setPhone(company.getPhone());
        dto.setAddress(company.getAddress());
        dto.setEmail(company.getEmail());
        return dto;
    }

    private Company convertToEntity(CompanyDTO dto) {
        Company company = new Company();
        company.setName(dto.getName());
        company.setPhone(dto.getPhone());
        company.setAddress(dto.getAddress());
        company.setEmail(dto.getEmail());
        return company;
    }

    private EmployeeDTO convertToEmployeeDTO(Employee employee) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(employee.getEmployeeId());
        dto.setFirstName(employee.getFirstName());
        dto.setLastName(employee.getLastName());
        dto.setPhone(employee.getPhone());
        dto.setEmployeeRole(employee.getEmployeeRole().name());
        dto.setCompanyId(employee.getCompany().getCompanyId());
        return dto;
    }

    private CustomerDTO convertToCustomerDTO(Customer customer) {
        CustomerDTO dto = new CustomerDTO();
        dto.setId(customer.getCustomerId());
        dto.setFirstName(customer.getFirstName());
        dto.setLastName(customer.getLastName());
        dto.setPhone(customer.getPhone());
        dto.setCustomerType(customer.getCustomerType().name());
        return dto;
    }

    private PropertyDTO convertToPropertyDTO(Property property) {
        PropertyDTO dto = new PropertyDTO();
        dto.setId(property.getPropertyId());
        dto.setEmployeeId(property.getEmployee().getEmployeeId());
        dto.setCustomerId(property.getCustomer().getCustomerId());
        dto.setTitle(property.getTitle());
        dto.setDescription(property.getDescription());
        dto.setPrice(property.getPrice());
        dto.setStatus(property.getStatus().name());
        dto.setPropertyType(property.getPropertyType());
        dto.setSquareMeters(property.getSquareMeters());
        dto.setRoomCount(property.getRoomCount());
        dto.setHeatingType(property.getHeatingType());
        dto.setFloorNumber(property.getFloorNumber());
        dto.setAddress(property.getAddress());
        return dto;
    }

    

    //Tüm şirket kayıtlarını döndürür.
    @Override
    @Transactional(readOnly = true)
    public List<CompanyDTO> getAllCompanies() {
        return companyRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CompanyDTO getCompanyById(UUID companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + companyId));
        return convertToDTO(company);
    }

    @Override
    @Transactional
    public CompanyDTO createCompany(CompanyDTO companyDTO) {
        Company company = convertToEntity(companyDTO);
        Company savedCompany = companyRepository.save(company);
        return convertToDTO(savedCompany);
    }

    /**
     * ID değeri verilen şirketi günceller.
     * Önce şirket var mı diye kontrol eder, varsa alanlarını günceller.
     */
    @Override
    @Transactional
    public CompanyDTO updateCompany(UUID companyId, CompanyDTO companyDTO) {
        Company existingCompany = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + companyId));

        existingCompany.setName(companyDTO.getName());
        existingCompany.setPhone(companyDTO.getPhone());
        existingCompany.setAddress(companyDTO.getAddress());
        existingCompany.setEmail(companyDTO.getEmail());

        Company updatedCompany = companyRepository.save(existingCompany);
        return convertToDTO(updatedCompany);
    }


    //ID değeri verilen şirketi siler.
    @Override
    @Transactional
    public void deleteCompany(UUID companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + companyId));
        companyRepository.delete(company);
    }

    @Override
    public CompanyDTO getLatestCompany() {
        Company company = companyRepository.findTopByOrderByCreatedAtDesc();
        if (company == null) {
            return null;
        }

        CompanyDTO dto = new CompanyDTO();
        dto.setId(company.getCompanyId());
        dto.setName(company.getName());
        dto.setAddress(company.getAddress());
        dto.setEmail(company.getEmail());
        dto.setPhone(company.getPhone());

        return dto;
    }

    
}
