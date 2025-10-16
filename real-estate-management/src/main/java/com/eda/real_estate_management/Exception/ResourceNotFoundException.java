package com.eda.real_estate_management.Exception;
//veri tabanında bulunamayan kaynaklar için özel bir istisna sınıfı
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
    
}
