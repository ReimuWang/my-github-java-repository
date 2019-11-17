package org.reimuwang.testspringboot.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.Resource;
import javax.sql.DataSource;

@Configuration
@Slf4j
public class DataSourceConfig {

    @Bean
    @ConfigurationProperties("foo.datasource")
    public DataSourceProperties fooDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Resource
    public DataSource fooDataSource(DataSourceProperties fooDataSourceProperties) {
        return fooDataSourceProperties.initializeDataSourceBuilder().build();
    }

    @Bean
    @Resource
    public PlatformTransactionManager fooTxManager(DataSource fooDataSource) {
        return new DataSourceTransactionManager(fooDataSource);
    }

    @Bean
    @Resource
    public JdbcTemplate fooJdbcTemplate(DataSource fooDataSource) {
        return new JdbcTemplate(fooDataSource);
    }

    @Bean
    @ConfigurationProperties("bar.datasource")
    public DataSourceProperties barDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Resource
    public DataSource barDataSource(DataSourceProperties barDataSourceProperties) {
        return barDataSourceProperties.initializeDataSourceBuilder().build();
    }

    @Bean
    @Resource
    public PlatformTransactionManager barTxManager(DataSource barDataSource) {
        return new DataSourceTransactionManager(barDataSource);
    }

    @Bean
    @Resource
    public JdbcTemplate barJdbcTemplate(DataSource barDataSource) {
        return new JdbcTemplate(barDataSource);
    }
}
