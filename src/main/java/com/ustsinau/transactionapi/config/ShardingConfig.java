package com.ustsinau.transactionapi.config;

import org.apache.shardingsphere.driver.api.yaml.YamlShardingSphereDataSourceFactory;
import org.springframework.context.annotation.*;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;


@Configuration
@Profile("local")
public class ShardingConfig {

    @Bean
    public DataSource dataSource() throws SQLException, IOException {
        File yamlFile = new ClassPathResource("sharding.yaml").getFile();
        return YamlShardingSphereDataSourceFactory.createDataSource(yamlFile);
    }
}
