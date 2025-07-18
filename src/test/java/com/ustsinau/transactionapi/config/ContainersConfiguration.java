package com.ustsinau.transactionapi.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.driver.api.yaml.YamlShardingSphereDataSourceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;


import javax.sql.DataSource;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;


@Slf4j
@Testcontainers
@Configuration
@Profile("test")
public class ContainersConfiguration {

    @Container
    private static final PostgreSQLContainer<?> container0 = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("ds0")
            .withUsername("postgres")
            .withPassword("postgres");

    @Container
    private static final PostgreSQLContainer<?> container1 = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("ds1")
            .withUsername("postgres")
            .withPassword("postgres");

    @Container
    private static final PostgreSQLContainer<?> container2 = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("ds2")
            .withUsername("postgres")
            .withPassword("postgres");

    @Container
    private static final PostgreSQLContainer<?> container3 = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("ds3")
            .withUsername("postgres")
            .withPassword("postgres");

    @Bean
    @Primary
    public DataSource dataSource() {
        try {
            Map<String, PostgreSQLContainer<?>> containerMap = Map.of(
                    "ds0", container0,
                    "ds1", container1,
                    "ds2", container2,
                    "ds3", container3
            );

            container0.start();
            container1.start();
            container2.start();
            container3.start();

            // Получаем порты контейнеров
            String db0Port = String.valueOf(container0.getMappedPort(5432));
            String db1Port = String.valueOf(container1.getMappedPort(5432));
            String db2Port = String.valueOf(container2.getMappedPort(5432));
            String db3Port = String.valueOf(container3.getMappedPort(5432));

            // Устанавливаем эти порты как свойства приложения
            System.setProperty("db0.port", db0Port);
            System.setProperty("db1.port", db1Port);
            System.setProperty("db2.port", db2Port);
            System.setProperty("db3.port", db3Port);

            StringBuilder yaml = new StringBuilder();
            yaml.append("dataSources:\n");

            for (var entry : containerMap.entrySet()) {
                String name = entry.getKey();
                PostgreSQLContainer<?> container = entry.getValue();

                yaml.append("  ").append(name).append(":\n");
                yaml.append("    dataSourceClassName: com.zaxxer.hikari.HikariDataSource\n");
                yaml.append("    driverClassName: org.postgresql.Driver\n");
                yaml.append("    jdbcUrl: jdbc:postgresql://")
                        .append(container.getHost()) // Используем host контейнера
                        .append(":")
                        .append(container.getMappedPort(5432)) // Используем порт, присвоенный контейнеру
                        .append("/").append(container.getDatabaseName()).append("\n");
                yaml.append("    username: ").append(container.getUsername()).append("\n");
                yaml.append("    password: ").append(container.getPassword()).append("\n");}

            //  Добавляем часть с rules
            InputStream rulesStream = new ClassPathResource("config-sharding-rules.yaml").getInputStream();
            String rules = new String(rulesStream.readAllBytes());

            yaml.append("\n").append(rules);

            // Создаем временный yaml
            Path tempFile = Files.createTempFile("sharding-config-", ".yaml");
            Files.writeString(tempFile, yaml.toString());

            log.info("Generated dynamic ShardingSphere config: \n{}", yaml);

            try {
                return YamlShardingSphereDataSourceFactory.createDataSource(tempFile.toFile());
            } catch (Exception ex) {
                log.error("Ошибка при создании ShardingSphere DataSource: {}", ex.getMessage(), ex);
                throw new RuntimeException("Failed to create ShardingSphere DataSource", ex);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to create ShardingSphere DataSource", e);
        }
    }
}
