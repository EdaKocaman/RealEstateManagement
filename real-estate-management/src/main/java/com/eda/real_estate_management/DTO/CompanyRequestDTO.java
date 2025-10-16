package com.eda.real_estate_management.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


// Controller'a gelen create/update taleplerinde kullanılacak DTO
public class CompanyRequestDTO {
    
    @NotBlank(message = "Company name is required")
    @Size(max = 100, message = "Company name can't exceed 100 characters")
    private String name;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "Phone is required")
    private String phone;

    // Getter ve Setter
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}
