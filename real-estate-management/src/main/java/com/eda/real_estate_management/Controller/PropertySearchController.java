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

import com.eda.real_estate_management.DTO.PropertyDTO;
import com.eda.real_estate_management.DTO.PropertySearchDTO;
import com.eda.real_estate_management.DTO.PropertySearchRequestDTO;
import com.eda.real_estate_management.Service.PropertySearchService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/property-searches")
@Tag(name = "Property Searches", description = "Property Search CRUD işlemleri")
public class PropertySearchController {

    private final PropertySearchService propertySearchService;

    public PropertySearchController(PropertySearchService propertySearchService) {
        this.propertySearchService = propertySearchService;
    }

    
    @Operation(summary = "Get all property searches", description = "Returns a list of all property searches")
    @GetMapping
    public ResponseEntity<List<PropertySearchDTO>> getAllSearches() {
        return ResponseEntity.ok(propertySearchService.getAllSearches());
    }

    
    @Operation(summary = "Get property search by ID", description = "Returns a specific property search by its ID")
    @GetMapping("/{searchId}")
    public ResponseEntity<PropertySearchDTO> getSearchById(@PathVariable UUID searchId) {
        return ResponseEntity.ok(propertySearchService.getSearchById(searchId));
    }

    
    @Operation(summary = "Create new property search", description = "Adds a new property search to the system")
    @PostMapping
    public ResponseEntity<PropertySearchDTO> createSearch(@Valid @RequestBody PropertySearchRequestDTO dto) {
        return ResponseEntity.ok(propertySearchService.createSearch(dto));
    }

    
    @Operation(summary = "Update property search", description = "Updates property search information by ID")
    @PutMapping("/{searchId}")
    public ResponseEntity<PropertySearchDTO> updateSearch(@PathVariable UUID searchId, @RequestBody PropertySearchRequestDTO dto) {
        return ResponseEntity.ok(propertySearchService.updateSearch(searchId, dto));
    }

    
    @Operation(summary = "Delete property search", description = "Deletes a property search by ID")
    @DeleteMapping("/{searchId}")
    public ResponseEntity<Void> deleteSearch(@PathVariable UUID searchId) {
        propertySearchService.deleteSearch(searchId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Search properties", description = "Search properties based on criteria")
    @PostMapping("/search")
    public ResponseEntity<List<PropertyDTO>> searchProperties(@RequestBody PropertySearchRequestDTO dto) {
        return ResponseEntity.ok(propertySearchService.searchProperties(dto));
    }

    @RestController
    @RequestMapping("/api/saved-searches")
    public class SavedSearchController {

        @PostMapping
        public ResponseEntity<String> saveSearch(@RequestBody PropertySearchRequestDTO dto) {
            // Burada dto'yu veritabanına kaydetme işlemleri yapılabilir
            return ResponseEntity.ok("Kriterler kaydedildi");
        }
    }

}
