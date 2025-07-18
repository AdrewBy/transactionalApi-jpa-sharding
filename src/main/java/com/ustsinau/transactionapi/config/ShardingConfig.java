package com.ustsinau.transactionapi.config;


import com.ustsinau.transactionapi.config.shardingAlgorithm.PaymentUserIdResolver;
import com.ustsinau.transactionapi.config.shardingAlgorithm.UserIdByPaymentShardingAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.*;

@Slf4j
@Configuration
public class ShardingConfig {

    @Bean
 //   @DependsOn("dataSource")
    public UserIdByPaymentShardingAlgorithm shardingAlgorithm(
            PaymentUserIdResolver resolver) {
        UserIdByPaymentShardingAlgorithm.setResolver(resolver);
        return new UserIdByPaymentShardingAlgorithm();
    }
}
