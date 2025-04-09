package com.ustsinau.transactionapi;

import org.springframework.boot.SpringApplication;
import org.testcontainers.utility.TestcontainersConfiguration;

public class TestTransactionApiApplication {

    public static void main(String[] args) {
        SpringApplication.from(TransactionApiApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
