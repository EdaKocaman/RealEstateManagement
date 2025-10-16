package com.eda.real_estate_management.Entity;


import java.math.BigDecimal;
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
@Table(name = "properties")
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
public class Property {

    @Id
    @GeneratedValue
    @Column(name = "property_id", updatable = false, nullable = false)
    private UUID propertyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "property_type", nullable = false)
    private String propertyType;

    @Column(name = "square_meters", nullable = false)
    private int squareMeters;

    @Column(name = "room_count", nullable = false)
    private String roomCount;

    @Column(name = "floor_number", nullable = false)
    private int floorNumber;

    @Column(name = "heating_type")
    private String heatingType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PropertyStatus status;

    public enum PropertyStatus {
        FOR_SALE,
        FOR_RENT,
        SOLD,
        RENTED
    }

    @Column(nullable = false)
    private BigDecimal price;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String title; 

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime createdAt;

}
