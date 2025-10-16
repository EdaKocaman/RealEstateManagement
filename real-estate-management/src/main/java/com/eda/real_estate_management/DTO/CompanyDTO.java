package com.eda.real_estate_management.DTO;

import java.util.UUID;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// Response DTO → dışarıya dönecek veri
public class CompanyDTO {
    private UUID id;

    @NotBlank(message = "Company name is required")
    @Size(min = 2, max = 100)
    private String name;

    @NotBlank(message = "Phone is required")
    @Size(min = 10, max = 15)
    private String phone;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    // Getter ve Setter
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
}
