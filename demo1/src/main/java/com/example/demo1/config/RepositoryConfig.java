package com.example.demo1.config;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableJpaRepositories(
    basePackages = "com.example.demo1.repository",
    entityManagerFactoryRef = "renderEntityManagerFactory",
    transactionManagerRef = "renderTransactionManager"
)
public class RepositoryConfig {
} 