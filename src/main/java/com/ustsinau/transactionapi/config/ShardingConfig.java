package com.ustsinau.transactionapi.config;


import org.springframework.context.annotation.*;
import org.apache.shardingsphere.driver.api.yaml.YamlShardingSphereDataSourceFactory;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;
import java.io.File;


@Configuration
@Profile("local")
public class ShardingConfig {

    @Bean
    public DataSource dataSource() throws Exception {
        File yamlFile = new ClassPathResource("sharding.yaml").getFile();
        return YamlShardingSphereDataSourceFactory.createDataSource(yamlFile);
    }

}
