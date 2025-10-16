//Veritabanı ile iletişim kurmak için kullanılır.
//JpaRepository, CRUD işlemlerini ve daha fazlasını sağlar.
// Repository interface olarak tanımlanır çünkü Spring Data JPA arka planda otomatik implementasyon sağlar.

package com.eda.real_estate_management.Repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.eda.real_estate_management.Entity.Property;

@Repository
public interface PropertyRepository extends JpaRepository<Property, UUID>, JpaSpecificationExecutor<Property> {
}
