package com.example.twodatasources.configuration;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
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
@EnableJpaRepositories(basePackages = "com.example.twodatasources.repository.first",
        entityManagerFactoryRef = "firstEntityManagerFactory",
        transactionManagerRef= "firstTransactionManager")
public class FirstDataSourceConfiguration {

    @Primary
    @Bean
    @ConfigurationProperties("spring.datasource.first")
    public DataSourceProperties firstDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Primary
    @Bean
    @ConfigurationProperties("spring.datasource.first.configuration")
    public DataSource firstDataSource(@Qualifier("firstDataSourceProperties") DataSourceProperties firstDataSourceProperties) {
        return firstDataSourceProperties.initializeDataSourceBuilder()
                .type(HikariDataSource.class).build();
    }

    @Primary
    @Bean(name = "firstEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean firstEntityManagerFactory(
            EntityManagerFactoryBuilder firstEntityManagerFactoryBuilder, @Qualifier("firstDataSource") DataSource firstDataSource) {

        Map<String, String> firstJpaProperties = new HashMap<>();
        firstJpaProperties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        firstJpaProperties.put("hibernate.hbm2ddl.auto", "update");

        return firstEntityManagerFactoryBuilder
                .dataSource(firstDataSource)
                .packages("com.example.twodatasources.model.first")
                .persistenceUnit("firstDataSource")
                .properties(firstJpaProperties)
                .build();
    }

    @Primary
    @Bean
    public PlatformTransactionManager firstTransactionManager(
            final @Qualifier("firstEntityManagerFactory") LocalContainerEntityManagerFactoryBean firstEntityManagerFactory) {
        return new JpaTransactionManager(firstEntityManagerFactory.getObject());
    }

}
