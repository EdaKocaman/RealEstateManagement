//Veritabanı ile iletişim kurmak için kullanılır.
//JpaRepository, CRUD işlemlerini ve daha fazlasını sağlar.
// Repository interface olarak tanımlanır çünkü Spring Data JPA arka planda otomatik implementasyon sağlar.

package com.eda.real_estate_management.Entity;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Bu sınıfın bir JPA entity olduğunu ve veritabanında bir tabloya karşılık geldiğini belirtir.
@Entity
// Tablo adını belirtir.
@Table(name = "customers")
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
public class Customer {

    @Id
    @GeneratedValue
    @Column(name = "customer_id", updatable = false, nullable = false)
    private UUID customerId;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(name = "customer_type", nullable = false)
    private CustomerType customerType;

    public enum CustomerType {
        BUYER,
        SELLER,
        BOTH
    }

    @Column(nullable = false)
    private String phone;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime createdAt;

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private List<Property> properties;

}
