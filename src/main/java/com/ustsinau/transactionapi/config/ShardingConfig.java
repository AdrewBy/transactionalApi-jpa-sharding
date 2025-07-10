package com.ustsinau.transactionapi.config;


import com.ustsinau.transactionapi.shardingAlgorithm.PaymentUserIdResolver;
import com.ustsinau.transactionapi.shardingAlgorithm.UserIdByPaymentShardingAlgorithm;
import org.apache.shardingsphere.infra.util.yaml.YamlEngine;
import org.apache.shardingsphere.sharding.yaml.config.YamlShardingRuleConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.apache.shardingsphere.driver.api.yaml.YamlShardingSphereDataSourceFactory;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;
import java.io.File;
import java.util.Map;
import java.util.UUID;


@Configuration
@Profile("local")
public class ShardingConfig {

    @Bean
    public DataSource dataSource() throws Exception {
        File yamlFile = new ClassPathResource("sharding.yaml").getFile();
        return YamlShardingSphereDataSourceFactory.createDataSource(yamlFile);
    }

    @Bean
    @DependsOn("dataSource")
    public UserIdByPaymentShardingAlgorithm shardingAlgorithm(
            PaymentUserIdResolver resolver) {
        UserIdByPaymentShardingAlgorithm.setResolver(resolver);
        return new UserIdByPaymentShardingAlgorithm();
    }

}
