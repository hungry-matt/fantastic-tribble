package com.example.demo.config.database.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter @Setter
@ConfigurationProperties(prefix = "datasource.demo")
public class PersistenceDemoProperties {
    private String username;
    private String password;
    private String driverClassName;
    private String hibernateDialect;
    private String jdbcUrl;
}
