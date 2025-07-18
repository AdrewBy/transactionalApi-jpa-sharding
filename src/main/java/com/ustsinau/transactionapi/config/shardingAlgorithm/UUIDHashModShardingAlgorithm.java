package com.ustsinau.transactionapi.config.shardingAlgorithm;


import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.sharding.api.sharding.standard.RangeShardingValue;

import org.apache.shardingsphere.sharding.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.StandardShardingAlgorithm;

import java.util.*;

@Slf4j
public class UUIDHashModShardingAlgorithm implements StandardShardingAlgorithm<UUID> {

    @Getter
    private Properties props = new Properties();
    private List<String> availableShards = List.of("ds0", "ds1", "ds2", "ds3");

    @Override
    public String doSharding(Collection<String> availableTargetNames,
                             PreciseShardingValue<UUID> shardingValue) {

        log.info("shardingValue: {}", shardingValue);

        UUID value = shardingValue.getValue();
        int shardIndex = Math.abs(value.hashCode()) % availableTargetNames.size();
        String selectedShard = new ArrayList<>(availableTargetNames).get(shardIndex);
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