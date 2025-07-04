package com.example.demo1.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class DataSourceConfig {

    @Value("${spring.datasource.url}")
    private String renderJdbcUrl;

    @Value("${spring.datasource.username}")
    private String renderUsername;

    @Value("${spring.datasource.password}")
    private String renderPassword;

    @Value("${spring.datasource.driver-class-name}")
    private String renderDriverClassName;

    @Value("${supabase.datasource.url}")
    private String supabaseJdbcUrl;

    @Value("${supabase.datasource.username}")
    private String supabaseUsername;

    @Value("${supabase.datasource.password}")
    private String supabasePassword;

    @Value("${supabase.datasource.driver-class-name}")
    private String supabaseDriverClassName;

    @Primary
    @Bean(name = "renderDataSource")
    public DataSource renderDataSource() {
        return DataSourceBuilder.create()
                .url(renderJdbcUrl)
                .username(renderUsername)
                .password(renderPassword)
                .driverClassName(renderDriverClassName)
                .build();
    }

    @Bean(name = "supabaseDataSource")
    public DataSource supabaseDataSource() {
        return DataSourceBuilder.create()
                .url(supabaseJdbcUrl)
                .username(supabaseUsername)
                .password(supabasePassword)
                .driverClassName(supabaseDriverClassName)
                .build();
    }

    @Primary
    @Bean(name = "renderJdbcTemplate")
    public JdbcTemplate renderJdbcTemplate(@Qualifier("renderDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean(name = "supabaseJdbcTemplate")
    public JdbcTemplate supabaseJdbcTemplate(@Qualifier("supabaseDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Primary
    @Bean(name = "renderEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean renderEntityManagerFactory(
            @Qualifier("renderDataSource") DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("com.example.demo1.entity");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        properties.setProperty("hibernate.hbm2ddl.auto", "none");
        properties.setProperty("hibernate.show_sql", "true");
        properties.setProperty("hibernate.format_sql", "true");
        em.setJpaProperties(properties);

        return em;
    }

    @Bean(name = "supabaseEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean supabaseEntityManagerFactory(
            @Qualifier("supabaseDataSource") DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("com.example.demo1.entity");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        properties.setProperty("hibernate.hbm2ddl.auto", "none");
        properties.setProperty("hibernate.show_sql", "true");
        properties.setProperty("hibernate.format_sql", "true");
        em.setJpaProperties(properties);

        return em;
    }

    @Primary
    @Bean(name = "renderTransactionManager")
    public PlatformTransactionManager renderTransactionManager(
            @Qualifier("renderEntityManagerFactory") LocalContainerEntityManagerFactoryBean entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory.getObject());
        return transactionManager;
    }

    @Bean(name = "supabaseTransactionManager")
    public PlatformTransactionManager supabaseTransactionManager(
            @Qualifier("supabaseEntityManagerFactory") LocalContainerEntityManagerFactoryBean entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory.getObject());
        return transactionManager;
    }
} 