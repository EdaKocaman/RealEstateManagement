package com.eda.real_estate_management.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class JpaConfig {
    // JPA otomatik timestamp veya auditing için gerekli yapılandırma
}
