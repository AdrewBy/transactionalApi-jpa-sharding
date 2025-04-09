package com.ustsinau.transactionapi;

import com.ustsinau.transactionapi.config.ContainersConfiguration;
import com.ustsinau.transactionapi.config.ShardingConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

@Import(ContainersConfiguration.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.yml")
@ContextConfiguration
class TransactionApiApplicationTests {

    @Test
    void contextLoads() {
    }

}
