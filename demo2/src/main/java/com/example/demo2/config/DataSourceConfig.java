package com.example.demo2.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Primary
    @Bean(name = "renderDataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource renderDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "supabaseDataSource")
    @ConfigurationProperties(prefix = "supabase.datasource")
    public DataSource supabaseDataSource() {
        return DataSourceBuilder.create().build();
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
} 