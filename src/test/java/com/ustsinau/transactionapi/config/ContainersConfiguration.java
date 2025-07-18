package com.ustsinau.transactionapi.config;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

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

@Slf4j
@Testcontainers
@Configuration
@Profile("test")
public class ContainersConfiguration {

    private static final int[] PORTS = {15432, 15433, 15434, 15435};
    public static final PostgreSQLContainer<?>[] CONTAINERS = new PostgreSQLContainer[4];

    static {
        for (int i = 0; i < 4; i++) {
            final int port = PORTS[i];
            CONTAINERS[i] = new PostgreSQLContainer<>("postgres:latest")
                    .withDatabaseName("ds" + i)
                    .withUsername("postgres")
                    .withPassword("postgres")
                    .withExposedPorts(5432)
                    .withCreateContainerCmdModifier(cmd ->
                            cmd.withHostConfig(
                                    new HostConfig().withPortBindings(
                                            new PortBinding(Ports.Binding.bindPort(port), new ExposedPort(5432))
                                    )
                            ));
            CONTAINERS[i].start();
        }
    }
}


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

//@Slf4j
//@Testcontainers
//@Configuration
//@Profile("test")
//public class ContainersConfiguration {
//
//    @Container
//    private static final PostgreSQLContainer<?> container0 = new PostgreSQLContainer<>("postgres:latest")
//            .withDatabaseName("ds0")
//            .withUsername("postgres")
//            .withPassword("postgres");
//
//    @Container
//    private static final PostgreSQLContainer<?> container1 = new PostgreSQLContainer<>("postgres:latest")
//            .withDatabaseName("ds1")
//            .withUsername("postgres")
//            .withPassword("postgres");
//
//    @Container
//    private static final PostgreSQLContainer<?> container2 = new PostgreSQLContainer<>("postgres:latest")
//            .withDatabaseName("ds2")
//            .withUsername("postgres")
//            .withPassword("postgres");
//
//    @Container
//    private static final PostgreSQLContainer<?> container3 = new PostgreSQLContainer<>("postgres:latest")
//            .withDatabaseName("ds3")
//            .withUsername("postgres")
//            .withPassword("postgres");
//
//    @Bean
//    @Primary
//    public DataSource dataSource() {
//        try {
//            Map<String, PostgreSQLContainer<?>> containerMap = Map.of(
//                    "ds0", container0,
//                    "ds1", container1,
//                    "ds2", container2,
//                    "ds3", container3
//            );
//
//            container0.start();
//            container1.start();
//            container2.start();
//            container3.start();
//
//            // Получаем порты контейнеров
//            String db0Port = String.valueOf(container0.getMappedPort(5432));
//            String db1Port = String.valueOf(container1.getMappedPort(5432));
//            String db2Port = String.valueOf(container2.getMappedPort(5432));
//            String db3Port = String.valueOf(container3.getMappedPort(5432));
//
//            // Устанавливаем эти порты как свойства приложения
//            System.setProperty("db0.port", db0Port);
//            System.setProperty("db1.port", db1Port);
//            System.setProperty("db2.port", db2Port);
//            System.setProperty("db3.port", db3Port);
//
//            StringBuilder yaml = new StringBuilder();
//            yaml.append("dataSources:\n");
//
//            for (var entry : containerMap.entrySet()) {
//                String name = entry.getKey();
//                PostgreSQLContainer<?> container = entry.getValue();
//
//                yaml.append("  ").append(name).append(":\n");
//                yaml.append("    dataSourceClassName: com.zaxxer.hikari.HikariDataSource\n");
//                yaml.append("    driverClassName: org.postgresql.Driver\n");
//                yaml.append("    jdbcUrl: jdbc:postgresql://")
//                        .append(container.getHost()) // Используем host контейнера
//                        .append(":")
//                        .append(container.getMappedPort(5432)) // Используем порт, присвоенный контейнеру
//                        .append("/").append(container.getDatabaseName()).append("\n");
//                yaml.append("    username: ").append(container.getUsername()).append("\n");
//                yaml.append("    password: ").append(container.getPassword()).append("\n");}
//
//            //  Добавляем часть с rules
//            InputStream rulesStream = new ClassPathResource("sharding-test.yaml").getInputStream();
//            String rules = new String(rulesStream.readAllBytes());
//
//            yaml.append("\n").append(rules);
//
//            // Создаем временный yaml
//            Path tempFile = Files.createTempFile("sharding-config-", ".yaml");
//            Files.writeString(tempFile, yaml.toString());
//
//            log.info("Generated dynamic ShardingSphere config: \n{}", yaml);
//
//            try {
//                return YamlShardingSphereDataSourceFactory.createDataSource(tempFile.toFile());
//            } catch (Exception ex) {
//                log.error("Ошибка при создании ShardingSphere DataSource: {}", ex.getMessage(), ex);
//                throw new RuntimeException("Failed to create ShardingSphere DataSource", ex);
//            }
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to create ShardingSphere DataSource", e);
//        }
//    }
//}
