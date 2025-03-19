//package com.ustsinau.transactionapi.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.DependsOn;
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
//import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
//
//import javax.sql.DataSource;
//
//@Configuration
//public class JpaConfig {
//
//    @Bean
//    @DependsOn({"flywayDb0", "flywayDb1", "flywayDb2", "flywayDb3"})
//    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
//        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
//        em.setDataSource(dataSource);
//        em.setPackagesToScan("com.ustsinau.transactionalapi"); // Укажите пакет с вашими сущностями
//        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
//        return em;
//    }
//}
