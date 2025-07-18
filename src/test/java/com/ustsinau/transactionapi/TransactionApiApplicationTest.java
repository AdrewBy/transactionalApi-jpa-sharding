package com.ustsinau.transactionapi;

import com.ustsinau.transactionapi.config.ContainersConfiguration;
import org.springframework.boot.SpringApplication;




public class TransactionApiApplicationTest {
    public static void main(String[] args) {
        SpringApplication.from(TransactionApiApplication::main).with(ContainersConfiguration.class).run(args);
    }
}