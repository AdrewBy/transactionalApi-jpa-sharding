package com.ustsinau.transactionapi.config.env;

import com.ustsinau.transactionapi.config.MigrationConfig;
import lombok.RequiredArgsConstructor;
import org.apache.shardingsphere.driver.api.yaml.YamlShardingSphereDataSourceFactory;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.*;
import org.springframework.core.io.ClassPathResource;


import javax.sql.DataSource;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@Profile("local")
@RequiredArgsConstructor
public class LocalConfig {

    private final MigrationConfig migrationConfig;

    @Bean(name = "physicalDataSources")
    public List<DataSource> physicalDataSources() {
        return migrationConfig.getDataSources().stream()
                .map(source -> DataSourceBuilder.create()
                        .url(source.getUrl())
                        .username(source.getUsername())
                        .password(source.getPassword())
                        .build())
                .collect(Collectors.toList());
    }

    @Bean
    @DependsOn("flywayMigrationInitializer")
    @Primary
    public DataSource dataSource() throws Exception {
        File yamlFile = new ClassPathResource("sharding-local.yaml").getFile();
        return YamlShardingSphereDataSourceFactory.createDataSource(yamlFile);
    }
}
