package com.eda.real_estate_management.Service;

import java.util.List;
import java.util.UUID;

import com.eda.real_estate_management.DTO.CompanyDTO;

/**
 * CompanyService, Company entity'si ile ilgili temel iş mantıklarını tanımlar.
 * Bu katmanda sadece metotların imzaları yer alır, implementasyon ayrı sınıfta yapılır.
 */


public interface CompanyService {

     List<CompanyDTO> getAllCompanies();
    CompanyDTO getCompanyById(UUID companyId);
    CompanyDTO createCompany(CompanyDTO companyDTO);
    CompanyDTO updateCompany(UUID companyId, CompanyDTO companyDTO);
    void deleteCompany(UUID companyId);
    CompanyDTO getLatestCompany();
}
