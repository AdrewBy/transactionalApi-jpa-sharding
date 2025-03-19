//package com.ustsinau.transactionapi.config;
//
//import org.springframework.boot.autoconfigure.flyway.FlywayDataSource;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.jdbc.datasource.DriverManagerDataSource;
//
//import javax.sql.DataSource;
//import java.util.List;
//
//@Configuration
//public class FlywayDataSourceConfig {
//
//    @Bean
//    @FlywayDataSource
//    public List<DataSource> flywayDataSources() {
//        return List.of(
//                createDataSource("jdbc:postgresql://localhost:5432/db0"),
//                createDataSource("jdbc:postgresql://localhost:5432/db1"),
//                createDataSource("jdbc:postgresql://localhost:5432/db2"),
//                createDataSource("jdbc:postgresql://localhost:5432/db3")
//        );
//    }
//
//    private DataSource createDataSource(String url) {
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName("org.postgresql.Driver");
//        dataSource.setUrl(url);
//        dataSource.setUsername("postgres");
//        dataSource.setPassword("postgres");
//        return dataSource;
//    }
//}
