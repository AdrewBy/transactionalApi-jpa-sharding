package com.ustsinau.transactionapi.shardingAlgorithm;


import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.sharding.api.sharding.hint.HintShardingAlgorithm;
import org.apache.shardingsphere.sharding.api.sharding.hint.HintShardingValue;

import java.util.*;

@Slf4j
public class MyHintShardingAlgorithm implements HintShardingAlgorithm<String> {

    @Getter
    private Properties props;
    private List<String> availableShards = List.of("ds0", "ds1", "ds2", "ds3");

    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames,
                                         HintShardingValue<String> shardingValue) {
        log.info("Sharding called for table: {}", shardingValue.getLogicTableName());
        log.info("Received hint values: {}", shardingValue.getValues());

        if (shardingValue.getValues().isEmpty()) {
            throw new IllegalStateException("No sharding hint provided");
        }

        UUID paymentUid = UUID.fromString(shardingValue.getValues().iterator().next());
//        UUID paymentUid = shardingValue.getValues().iterator().next();
        int shardIndex = Math.abs(paymentUid.hashCode()) % availableTargetNames.size();
        String selectedShard = new ArrayList<>(availableTargetNames).get(shardIndex);

        log.info("Selected shard: {}", selectedShard);
        return Collections.singleton(selectedShard);
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
}