//package com.ustsinau.transactionapi.config;
//
//
//import jakarta.annotation.PostConstruct;
//import org.flywaydb.core.Flyway;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//
//
//@Configuration
//public class FlywayConfig {
//
//    @Bean
//    public Flyway flywayDb0() {
//        return Flyway.configure()
//                .dataSource("jdbc:postgresql://localhost:5432/db0", "postgres", "postgres")
//                .locations("classpath:db/migration/ds0")
//                .schemas("transaction")
//                .load();
//    }
//
//    @Bean
//    public Flyway flywayDb1() {
//        return Flyway.configure()
//                .dataSource("jdbc:postgresql://localhost:5432/db1", "postgres", "postgres")
//                .locations("classpath:db/migration/ds1")
//                .schemas("transaction")
//                .load();
//    }
//
//    @Bean
//    public Flyway flywayDb2() {
//        return Flyway.configure()
//                .dataSource("jdbc:postgresql://localhost:5432/db2", "postgres", "postgres")
//                .locations("classpath:db/migration/ds2")
//                .schemas("transaction")
//                .load();
//    }
//
//    @Bean
//    public Flyway flywayDb3() {
//        return Flyway.configure()
//                .dataSource("jdbc:postgresql://localhost:5432/db3", "postgres", "postgres")
//                .locations("classpath:db/migration/ds3")
//                .schemas("transaction")
//                .load();
//    }
//
//    @PostConstruct
//    public void migrateAll() {
//        flywayDb0().migrate();
//        flywayDb1().migrate();
//        flywayDb2().migrate();
//        flywayDb3().migrate();
//    }
//}
