package com.eda.real_estate_management.Entity;

import java.time.ZonedDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Bu sınıfın bir JPA entity olduğunu ve veritabanında bir tabloya karşılık geldiğini belirtir.
@Entity
// Tablo adını belirtir.
@Table(name = "employees")
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
public class Employee {

    @Id
    @GeneratedValue
    @Column(name = "employee_id", updatable = false, nullable = false)
    private UUID employeeId;

    // Company ile ManyToOne ilişkisi
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false, referencedColumnName = "company_id")
    private Company company;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;


    @Column(nullable = false)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private EmployeeRole employeeRole;

    public enum EmployeeRole {
        STAFF,
        ADMIN
    }

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime createdAt;
    
}
