package com.example.demo.config.database.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter @Setter
@ConfigurationProperties(prefix = "datasource.default")
public class PersistenceDefaultProperties {
    private int hikariMaximumPoolSize;
    private int hikariMinimumIdle;
    private long hikariConnectionTimeout;
    private String jpaShowSql;
    private String jpaOpenInView;
    private String jpaHibernateDdlAuto;
}
