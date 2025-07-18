package com.ustsinau.transactionapi.config.shardingAlgorithm;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.sharding.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.RangeShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.StandardShardingAlgorithm;


import java.util.*;


@Slf4j
public class UserIdByPaymentShardingAlgorithm implements StandardShardingAlgorithm<UUID> {

    @Getter
    private Properties props;
    private List<String> availableShards = List.of("ds0", "ds1", "ds2", "ds3");

    private static PaymentUserIdResolver resolver;

    public static void setResolver(PaymentUserIdResolver resolver) {
        UserIdByPaymentShardingAlgorithm.resolver = resolver;
    }

    @Override
    public String doSharding(Collection<String> availableTargetNames,
                             PreciseShardingValue<UUID> shardingValue) {
        if (resolver == null) {
            throw new IllegalStateException("PaymentUserIdResolver not initialized");
        }
        log.info("shardingValue: {}", shardingValue);
        UUID userId = resolver.findUserIdByPaymentUid(shardingValue.getValue());
        int shard = Math.abs(userId.hashCode()) % availableTargetNames.size();
        String selectedShard = new ArrayList<>(availableTargetNames).get(shard);
        log.info("Standard routing to shard: {}", selectedShard);
        return selectedShard;
    }

    @Override
    public void init(Properties properties) {
        this.props = properties;
        if (properties.containsKey("shards")) {
            availableShards = Arrays.asList(properties.getProperty("shards").split(","));
        }
        log.info("Initialized with shards: {}", availableShards);
    }


    @Override
    public String getType() {
        return "CLASS_BASED";
    }

    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames,
                                         RangeShardingValue<UUID> shardingValue) {
        throw new UnsupportedOperationException("Range sharding is not supported for UUID hashing");
    }
}
