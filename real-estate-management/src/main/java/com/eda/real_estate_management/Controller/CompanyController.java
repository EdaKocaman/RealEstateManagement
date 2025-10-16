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

import com.eda.real_estate_management.DTO.CompanyDTO;
import com.eda.real_estate_management.Service.CompanyService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;



@RestController //REST API isteklerini işlemek için denetleyici sınıfı
@RequestMapping("/api/companies") 
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }
    

    @Operation(summary = "Get all companies")
    @GetMapping
    public ResponseEntity<List<CompanyDTO>> getAllCompanies() {
        return ResponseEntity.ok(companyService.getAllCompanies());
    }


    //ID ile şirket arama
    @Operation(summary = "Get company by ID")
    @GetMapping("/{companyId}")
    public ResponseEntity<CompanyDTO> getCompanyById(@PathVariable UUID companyId) {
        return ResponseEntity.ok(companyService.getCompanyById(companyId));
    }


    //Yeni şirket ekleme
    @Operation(summary = "Create a new company")
    @PostMapping
    public ResponseEntity<CompanyDTO> createCompany(@Valid @RequestBody CompanyDTO companyDTO) {
        return ResponseEntity.ok(companyService.createCompany(companyDTO));
    }

    
    //şirket bilgilerini güncelleme
    @Operation(summary = "Update an existing company")
    @PutMapping("/{companyId}")
    public ResponseEntity<CompanyDTO> updateCompany(@PathVariable UUID companyId,
                                                    @RequestBody CompanyDTO companyDTO) {
        return ResponseEntity.ok(companyService.updateCompany(companyId, companyDTO));
    }

    //şirket silme
    @Operation(summary = "Delete a company by ID")
    @DeleteMapping("/{companyId}")
    public ResponseEntity<Void> deleteCompany(@PathVariable UUID companyId) {
        companyService.deleteCompany(companyId);
        return ResponseEntity.noContent().build();
    } 
    
    @Operation(summary = "Get latest company by created date")
    @GetMapping("/latest")
    public ResponseEntity<CompanyDTO> getLatestCompany() {
        CompanyDTO latestCompany = companyService.getLatestCompany();
        if (latestCompany == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(latestCompany);
    }
}
