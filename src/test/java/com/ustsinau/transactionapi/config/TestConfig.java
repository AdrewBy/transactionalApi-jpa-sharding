package com.ustsinau.transactionapi.config;


import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.driver.api.yaml.YamlShardingSphereDataSourceFactory;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.*;
import org.springframework.core.io.ClassPathResource;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.ustsinau.transactionapi.config.ContainersConfiguration.*;

@Slf4j
@Configuration
@Profile("test")
@RequiredArgsConstructor
public class TestConfig {


//    @Bean(name = "physicalDataSources")
//    public List<DataSource> physicalDataSources() {
//        return Arrays.stream(ContainersConfiguration.CONTAINERS)
//                .map(container -> {
//                    DataSource ds = DataSourceBuilder.create()
//                            .url(container.getJdbcUrl())
//                            .username(container.getUsername())
//                            .password(container.getPassword())
//                            .build();
//                    log.info("Created physical DataSource for {}", container.getJdbcUrl());
//                    return ds;
//                })
//                .collect(Collectors.toList());
//    }
//
//    // 4. Основной DataSource с ShardingSphere
//    @Bean
//    @DependsOn({"flywayMigrationInitializer", "physicalDataSources"})
//    @Primary
//    public DataSource dataSource() throws Exception {
//        log.info("Creating ShardingSphere DataSource...");
//        File yamlFile = new ClassPathResource("sharding-test.yaml").getFile();
//        DataSource dataSource = YamlShardingSphereDataSourceFactory.createDataSource(yamlFile);
//        log.info("ShardingSphere DataSource created successfully");
//        return dataSource;
//    }

//    @Bean(name = "physicalDataSources")
//    public List<DataSource> physicalDataSources() {
//        return Arrays.stream(ContainersConfiguration.CONTAINERS)
//                .map(container -> {
//                    DataSource ds = DataSourceBuilder.create()
//                            .url(container.getJdbcUrl())
//                            .username(container.getUsername())
//                            .password(container.getPassword())
//                            .build();
//                    log.info("Created physical DataSource for {}", container.getJdbcUrl());
//                    return ds;
//                })
//                .collect(Collectors.toList());
//    }
}
