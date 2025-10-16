package com.eda.real_estate_management.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {
    
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // tüm endpointler için geçerli
                        .allowedOrigins("http://localhost:5173") // React portu
                        .allowedMethods("GET", "POST", "PUT", "DELETE"); // izin verilen HTTP metodları
            }
        };
    }
    
}
