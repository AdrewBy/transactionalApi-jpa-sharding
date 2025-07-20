package com.ustsinau.transactionapi.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.driver.api.yaml.YamlShardingSphereDataSourceFactory;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.*;
import org.springframework.core.io.ClassPathResource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Testcontainers
@Configuration
@Profile("test")
public class ContainersConfiguration {

    private static final PostgreSQLContainer<?>[] CONTAINERS = new PostgreSQLContainer[4];

    static {
        for (int i = 0; i < 4; i++) {
            CONTAINERS[i] = new PostgreSQLContainer<>("postgres:latest")
                    .withDatabaseName("ds" + i)
                    .withUsername("postgres")
                    .withPassword("postgres")
                    .withExposedPorts(5432); // Используем стандартный порт PostgreSQL внутри контейнера

            CONTAINERS[i].start();
            log.info("Started PostgreSQL container ds{} on host port {}",
                    i, CONTAINERS[i].getMappedPort(5432));
        }
    }

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
        String yamlContent = new String(Files.readAllBytes(new ClassPathResource("sharding-test.yaml").getFile().toPath()));

        // Заменяем плейсхолдеры на реальные порты
        for (int i = 0; i < 4; i++) {
            yamlContent = yamlContent.replace("${CONTAINERS["+i+"].getMappedPort(5432)}",
                    String.valueOf(CONTAINERS[i].getMappedPort(5432)));
        }

        DataSource dataSource = YamlShardingSphereDataSourceFactory.createDataSource(yamlContent.getBytes());
        log.info("ShardingSphere DataSource created successfully");
        return dataSource;
    }
}

//@Slf4j
//@Testcontainers
//@Configuration
//@Profile("test")
//public class ContainersConfiguration {
//
//    private static final int[] PORTS = {15432, 15433, 15434, 15435};
//    private static final PostgreSQLContainer<?>[] CONTAINERS = new PostgreSQLContainer[4];
//    private static final String DB_NAME_PREFIX = "ds";
//    private static final String DB_USER = "postgres";
//    private static final String DB_PASSWORD = "postgres";
//
//    static {
//        log.info("Starting PostgreSQL test containers...");
//        for (int i = 0; i < CONTAINERS.length; i++) {
//            final int port = PORTS[i];
//            CONTAINERS[i] = new PostgreSQLContainer<>("postgres:latest")
//                    .withDatabaseName(DB_NAME_PREFIX + i)
//                    .withUsername(DB_USER)
//                    .withPassword(DB_PASSWORD)
//                    .withExposedPorts(5432)
//                    .withCreateContainerCmdModifier(cmd ->
//                            cmd.withHostConfig(
//                                    new HostConfig().withPortBindings(
//                                            new PortBinding(Ports.Binding.bindPort(port), new ExposedPort(5432))
//
//                                    )
//                            ));
//            CONTAINERS[i].start();
//            log.info("Started container {} on port {}", CONTAINERS[i].getDatabaseName(), PORTS[i]);
//        }
//    }
//
//    @Bean
//    @DependsOn("flywayMigrationInitializer")
//    @Primary
//    public DataSource dataSource() {
//        try {
//            log.info("Creating ShardingSphere data source...");
//            File yamlFile = new ClassPathResource("sharding-test.yaml").getFile();
//            DataSource dataSource = YamlShardingSphereDataSourceFactory.createDataSource(yamlFile);
//            log.info("ShardingSphere data source created successfully");
//            return dataSource;
//        } catch (IOException | SQLException e) {
//            log.error("Failed to create ShardingSphere data source", e);
//            throw new IllegalStateException("Failed to initialize ShardingSphere data source", e);
//        }
//    }
//}


//@Slf4j
//@Testcontainers
//@Configuration
//@Profile("test")
//public class ContainersConfiguration {
//
//    // Фиксированные порты для каждого контейнера
//    private static final int[] PORTS = {15432, 15433, 15434, 15435};
//    private static final PostgreSQLContainer<?>[] CONTAINERS = new PostgreSQLContainer[4];
//
//    static {
//        // Инициализация контейнеров в статическом блоке
//        for (int i = 0; i < 4; i++) {
//            final int port = PORTS[i]; // Финализируем переменную для использования в лямбде
//            CONTAINERS[i] = new PostgreSQLContainer<>("postgres:latest")
//                    .withDatabaseName("ds" + i)
//                    .withUsername("postgres")
//                    .withPassword("postgres")
//                    .withExposedPorts(5432)
//                    .withCreateContainerCmdModifier(cmd ->
//                            cmd.withHostConfig(
//                                    new HostConfig().withPortBindings(
//                                            new PortBinding(Ports.Binding.bindPort(port), new ExposedPort(5432))
//                                    )
//                            ));
//            CONTAINERS[i].start();
//        }
//    }
//
//    @Bean
//    @Primary
//    public DataSource dataSource() {
//        try {
//            StringBuilder yaml = new StringBuilder("dataSources:\n");
//
//            for (int i = 0; i < 4; i++) {
//                yaml.append("  ds").append(i).append(":\n")
//                        .append("    dataSourceClassName: com.zaxxer.hikari.HikariDataSource\n")
//                        .append("    driverClassName: org.postgresql.Driver\n")
//                        .append("    jdbcUrl: jdbc:postgresql://")
//                        .append(CONTAINERS[i].getHost())
//                        .append(":")
//                        .append(PORTS[i])
//                        .append("/ds").append(i).append("\n")
//                        .append("    username: postgres\n")
//                        .append("    password: postgres\n");
//            }
//
//            // Добавляем правила шардинга
//            yaml.append(new String(Files.readAllBytes(
//                    Path.of("src/test/resources/sharding-test.yaml"))));
//
//            Path tempFile = Files.createTempFile("sharding-config", ".yaml");
//            Files.writeString(tempFile, yaml.toString());
//
//            log.info("ShardingSphere configuration:\n{}", yaml);
//
//            return YamlShardingSphereDataSourceFactory.createDataSource(tempFile.toFile());
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to create ShardingSphere DataSource", e);
//        }
//    }
//}


