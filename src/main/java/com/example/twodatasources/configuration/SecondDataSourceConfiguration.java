package com.example.twodatasources.configuration;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.example.twodatasources.repository.second",
        entityManagerFactoryRef = "secondEntityManagerFactory",
        transactionManagerRef= "secondTransactionManager")
public class SecondDataSourceConfiguration {

    @Bean
    @ConfigurationProperties("spring.datasource.second")
    public DataSourceProperties secondDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties("spring.datasource.second.configuration")
    public DataSource secondDataSource(@Qualifier("secondDataSourceProperties") DataSourceProperties secondDataSourceProperties) {
        return secondDataSourceProperties.initializeDataSourceBuilder()
                .type(HikariDataSource.class).build();
    }

    @Bean(name = "secondEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean secondEntityManagerFactory(
            EntityManagerFactoryBuilder secondEntityManagerFactoryBuilder, @Qualifier("secondDataSource") DataSource secondDataSource) {

        Map<String, String> secondJpaProperties = new HashMap<>();
        secondJpaProperties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        secondJpaProperties.put("hibernate.hbm2ddl.auto", "update");

        return secondEntityManagerFactoryBuilder
                .dataSource(secondDataSource)
                .packages("com.example.twodatasources.model.second")
                .persistenceUnit("secondJpaProperties")
                .properties(secondJpaProperties)
                .build();
    }

    @Bean
    public PlatformTransactionManager secondTransactionManager(
            final @Qualifier("secondEntityManagerFactory") LocalContainerEntityManagerFactoryBean secondEntityManagerFactory) {
        return new JpaTransactionManager(secondEntityManagerFactory.getObject());
    }

}
