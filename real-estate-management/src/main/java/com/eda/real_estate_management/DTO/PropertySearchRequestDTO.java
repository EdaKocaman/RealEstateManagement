package com.eda.real_estate_management.DTO;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.PositiveOrZero;

public class PropertySearchRequestDTO {
    
    private UUID customerId;
    private String propertyType;

    @PositiveOrZero(message = "Min price must be zero or positive")
    private BigDecimal minPrice;

    @PositiveOrZero(message = "Max price must be zero or positive")
    private BigDecimal maxPrice;

    @PositiveOrZero(message = "Min square meters must be zero or positive")
    private Integer minSquareMeters;

    @PositiveOrZero(message = "Max square meters must be zero or positive")
    private Integer maxSquareMeters;

    private String roomCount;
    private String heatingType;
    private String status;

    // Getter & Setter
    public UUID getCustomerId() { return customerId; }
    public void setCustomerId(UUID customerId) { this.customerId = customerId; }

    public String getPropertyType() { return propertyType; }
    public void setPropertyType(String propertyType) { this.propertyType = propertyType; }

    public BigDecimal getMinPrice() { return minPrice; }
    public void setMinPrice(BigDecimal minPrice) { this.minPrice = minPrice; }

    public BigDecimal getMaxPrice() { return maxPrice; }
    public void setMaxPrice(BigDecimal maxPrice) { this.maxPrice = maxPrice; }

    public Integer getMinSquareMeters() { return minSquareMeters; }
    public void setMinSquareMeters(Integer minSquareMeters) { this.minSquareMeters = minSquareMeters; }

    public Integer getMaxSquareMeters() { return maxSquareMeters; }
    public void setMaxSquareMeters(Integer maxSquareMeters) { this.maxSquareMeters = maxSquareMeters; }

    public String getRoomCount() { return roomCount; }
    public void setRoomCount(String roomCount) { this.roomCount = roomCount; }

    public String getHeatingType() { return heatingType; }
    public void setHeatingType(String heatingType) { this.heatingType = heatingType; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
