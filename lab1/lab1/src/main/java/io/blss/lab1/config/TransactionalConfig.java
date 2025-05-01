//package io.blss.lab1.config;
//
//import jakarta.transaction.TransactionManager;
//import jakarta.transaction.UserTransaction;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.transaction.PlatformTransactionManager;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//import org.springframework.transaction.jta.JtaTransactionManager;
//
//@Configuration
//@EnableTransactionManagement
//public class TransactionalConfig {
//
//    @Bean
//    public UserTransaction userTransaction() {
//        return com.arjuna.ats.jta.UserTransaction.userTransaction();
//    }
//
//    @Bean
//    public TransactionManager narayanaTransactionManager() {
//        return com.arjuna.ats.jta.TransactionManager.transactionManager();
//    }
//
//    @Bean(name = "transactionManager")
//    @Primary
//    public PlatformTransactionManager jtaTransactionManager() {
//        JtaTransactionManager jtaTransactionManager = new JtaTransactionManager();
//        jtaTransactionManager.setTransactionManager(narayanaTransactionManager());
//        jtaTransactionManager.setUserTransaction(userTransaction());
//        jtaTransactionManager.setAllowCustomIsolationLevels(true);
//
//        jtaTransactionManager.setNestedTransactionAllowed(true);
//        jtaTransactionManager.setDefaultTimeout(60);
//
//        return jtaTransactionManager;
//    }
//}