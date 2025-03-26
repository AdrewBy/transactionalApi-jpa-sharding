package com.ustsinau.transactionapi.shardingAlgorithm;

import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.sharding.api.sharding.standard.RangeShardingValue;

import org.apache.shardingsphere.sharding.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.StandardShardingAlgorithm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;


@Slf4j
public class UUIDHashModShardingAlgorithm implements StandardShardingAlgorithm<UUID> {

    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<UUID> shardingValue) {
        log.info("Available shards: {}", availableTargetNames);
        log.info("Sharding column: {}, value: {}", shardingValue.getColumnName(), shardingValue.getValue());

        // Ваш текущий алгоритм шардирования
        String shard = calculateShard(availableTargetNames, shardingValue.getValue());

        log.info("Selected shard: {}", shard);
        return shard;
    }

    private String calculateShard(Collection<String> availableTargetNames, UUID value) {
        int hash = Math.abs(value.hashCode()); // Хешируем UUID
        int shardIndex = hash % availableTargetNames.size(); // Определяем индекс шарда
        return new ArrayList<>(availableTargetNames).get(shardIndex); // Берём нужный шард
    }

    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, RangeShardingValue<UUID> shardingValue) {
        throw new UnsupportedOperationException("Range sharding is not supported for UUID hashing");
    }

}
