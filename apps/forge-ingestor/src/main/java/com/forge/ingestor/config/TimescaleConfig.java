package com.forge.ingestor.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import javax.sql.DataSource;

@Configuration
public class TimescaleConfig {

    @Value("${timescale.datasource.url}")
    private String url;

    @Value("${timescale.datasource.username}")
    private String username;

    @Value("${timescale.datasource.password}")
    private String password;

    @Bean
    public DataSource timescaleDataSource() {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName("org.postgresql.Driver");
        ds.setUrl(url);
        ds.setUsername(username);
        ds.setPassword(password);
        return ds;
    }

    // Named bean — matchar parametern i TimescaleService
    @Bean
    public JdbcTemplate timescaleJdbcTemplate(DataSource timescaleDataSource) {
        return new JdbcTemplate(timescaleDataSource);
    }
}