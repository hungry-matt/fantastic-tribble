package com.example.demo.config.database;

import com.example.demo.config.database.model.PersistenceConstant;
import com.example.demo.config.database.model.PersistenceDefaultProperties;
import com.example.demo.config.database.model.PersistenceDemoProperties;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Properties;

@Configuration
@EnableJpaRepositories(
        basePackages = {},
        entityManagerFactoryRef = "demoEntityManager",
        transactionManagerRef = "demoTransactionManager"
)
@RequiredArgsConstructor
@EnableTransactionManagement
public class PersistenceDemoConfiguration {
    private final PersistenceDemoProperties persistenceDemoProperties;
    private final PersistenceDefaultProperties persistenceDefaultProperties;

    @Bean
    @Qualifier("demoEntityManager")
    public LocalContainerEntityManagerFactoryBean demoEntityManager() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(demoDataSource());
        em.setPackagesToScan();

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setPersistenceUnitName("demo");

        Properties jpaProperties = new Properties();
        jpaProperties.setProperty(PersistenceConstant.NAMING_STRATEGY, "org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy");
        jpaProperties.setProperty(PersistenceConstant.OPEN_IN_VIEW, persistenceDefaultProperties.getJpaOpenInView());
        jpaProperties.setProperty(PersistenceConstant.DDL_AUTO, persistenceDefaultProperties.getJpaHibernateDdlAuto());
        jpaProperties.setProperty(PersistenceConstant.DIALECT, persistenceDemoProperties.getHibernateDialect());
        jpaProperties.setProperty(PersistenceConstant.SHOW_SQL, persistenceDefaultProperties.getJpaShowSql());

        em.setJpaProperties(jpaProperties);

        return em;
    }

    @Bean
    @Qualifier("demoDataSource")
    public HikariDataSource demoDataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(persistenceDemoProperties.getDriverClassName());
        hikariConfig.setJdbcUrl(persistenceDemoProperties.getJdbcUrl());
        hikariConfig.setUsername(persistenceDemoProperties.getUsername());
        hikariConfig.setPassword(persistenceDemoProperties.getPassword());
        hikariConfig.setMinimumIdle(persistenceDefaultProperties.getHikariMinimumIdle());
        hikariConfig.setMaximumPoolSize(persistenceDefaultProperties.getHikariMaximumPoolSize());
        hikariConfig.setConnectionTimeout(persistenceDefaultProperties.getHikariConnectionTimeout());
        return new HikariDataSource(hikariConfig);
    }

    @Bean
    @Qualifier("demoTransactionManager")
    public PlatformTransactionManager demoTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(demoEntityManager().getObject());
        return transactionManager;
    }
}
