package com.ustsinau.transactionapi.config;

import com.ustsinau.transactionapi.config.MigrationConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.driver.api.yaml.YamlShardingSphereDataSourceFactory;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.*;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Configuration
@Profile("test")
@RequiredArgsConstructor
public class TestConfig {

//    private final MigrationConfig migrationConfig;
//
//    private final ContainersConfiguration containersConfig;

    @Bean(name = "physicalDataSources")
    public List<DataSource> physicalDataSources() {
        return Arrays.stream(ContainersConfiguration.CONTAINERS)
                .map(container -> {
                    DataSource ds = DataSourceBuilder.create()
                            .url(container.getJdbcUrl())
                            .username(container.getUsername())
                            .password(container.getPassword())
                            .build();
                    log.info("Created physical DataSource for {}", container.getJdbcUrl());
                    return ds;
                })
                .collect(Collectors.toList());
    }

    // 4. Основной DataSource с ShardingSphere
    @Bean
    @DependsOn({"flywayMigrationInitializer", "physicalDataSources"})
    @Primary
    public DataSource dataSource() throws Exception {
        log.info("Creating ShardingSphere DataSource...");
        File yamlFile = new ClassPathResource("sharding-test.yaml").getFile();
        DataSource dataSource = YamlShardingSphereDataSourceFactory.createDataSource(yamlFile);
        log.info("ShardingSphere DataSource created successfully");
        return dataSource;
    }

    // 5. Тестовая версия алгоритма шардирования
//    @Bean
//    @Primary
//    public UserIdByPaymentShardingAlgorithm testShardingAlgorithm() {
//        log.info("Creating test sharding algorithm");
//        UserIdByPaymentShardingAlgorithm algorithm = new UserIdByPaymentShardingAlgorithm();
//        algorithm.setResolver(new TestPaymentUserIdResolver());
//        return algorithm;
//    }
}
