package com.ustsinau.transactionapi.config;


import com.ustsinau.transactionapi.entity.MigrationSource;
import lombok.Data;
import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationInitializer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "flyway")
public class MigrationConfig {

    private List<MigrationSource> dataSources;

    @Bean
    @DependsOn("dataSource")
    public FlywayMigrationInitializer flywayMigrationInitializer() {
        return new FlywayMigrationInitializer(Flyway.configure().load(), x -> {
            for (MigrationSource source : dataSources) {
                Flyway.configure()
                        .dataSource(source.getUrl(), source.getUsername(), source.getPassword())
                        .locations("classpath:db/migration")
                        .load().migrate();
            }
        });
    }

}