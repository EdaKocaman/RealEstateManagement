package com.eda.real_estate_management.Entity;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import com.eda.real_estate_management.Entity.Property.PropertyStatus;

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
@Table(name = "property_searches")
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
public class PropertySearch {
    
    @Id
    @GeneratedValue
    @Column(name = "search_id", updatable = false, nullable = false)
    private UUID searchId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "property_type", nullable = false)
    private String propertyType;

    @Column(name = "min_price")
    private BigDecimal minPrice;

    @Column(name = "max_price")
    private BigDecimal maxPrice;

    @Column(name = "min_square_meters")
    private Integer minSquareMeters;

    @Column(name = "max_square_meters")
    private Integer maxSquareMeters;

    @Column(name = "room_count")
    private String roomCount;

    @Column(name = "heating_type")
    private String heatingType;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime createdAt;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PropertyStatus status;
}
