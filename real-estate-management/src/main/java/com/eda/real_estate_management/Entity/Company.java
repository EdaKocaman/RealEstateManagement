package com.eda.real_estate_management.Entity;

import java.time.ZonedDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Bu sınıfın bir JPA entity olduğunu ve veritabanında bir tabloya karşılık geldiğini belirtir.
@Entity
// Tablo adını belirtir.
@Table(name = "company")
// Lombok ile sınıftaki tüm alanlar için otomatik getter metotları oluşturur.
@Getter
// Lombok ile sınıftaki tüm alanlar için otomatik setter metotları oluşturur.
@Setter
// Default constructor oluşturur.
@NoArgsConstructor
// Tüm alanları parametre olarak alan bir constructor oluşturur.
@AllArgsConstructor
// Builder tasarım desenini kullanarak nesne oluşturmayı sağlar.
@Builder
public class Company {

    @Id
    @GeneratedValue
    @Column(name = "company_id", updatable = false, nullable = false)
    private UUID companyId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String email;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime createdAt;
}
