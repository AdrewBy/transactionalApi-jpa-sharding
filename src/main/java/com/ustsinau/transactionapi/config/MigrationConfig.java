package com.ustsinau.transactionapi.config;


import com.ustsinau.transactionapi.entity.MigrationSource;
import lombok.Data;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationInitializer;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.sql.DataSource;
import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "flyway")
public class MigrationConfig {

    private List<MigrationSource> dataSources;

    @Bean
    @DependsOn("physicalDataSources") // Зависит от физических DataSource
    public Flyway flyway(@Qualifier("physicalDataSources") List<DataSource> dataSources) {
        // Применяем миграции ко всем физическим базам
        dataSources.forEach(ds -> {
            Flyway configuredFlyway = Flyway.configure()
                    .dataSource(ds)
                    .locations("classpath:db/migration")
                    .baselineOnMigrate(true)
                    .load();
            configuredFlyway.migrate();
        });

        // Возвращаем Flyway для основной БД (может быть заглушкой)
        return Flyway.configure()
                .dataSource(dataSources.get(0))
                .locations("classpath:db/migration")
                .load();
    }

    @Bean
    public FlywayMigrationInitializer flywayMigrationInitializer(Flyway flyway) {
        return new FlywayMigrationInitializer(flyway, f -> {});
    }

    @Data
    public static class MigrationSource {
        private String url;
        private String username;
        private String password;
    }
}

//    @Bean
//    public FlywayMigrationInitializer flywayMigrationInitializer() {
//        return new FlywayMigrationInitializer(Flyway.configure().load(), x -> {
//            for (MigrationSource source : dataSources) {
//                Flyway.configure()
//                        .dataSource(source.getUrl(), source.getUsername(), source.getPassword())
//                        .locations("classpath:db/migration")
//                        .load().migrate();
//            }
//        });
//    }

//}