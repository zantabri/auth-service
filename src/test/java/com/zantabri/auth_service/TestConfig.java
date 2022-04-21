package com.zantabri.auth_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Profile("test")
@Configuration
@EnableJpaRepositories
@PropertySource({"classpath:db/hbn.properties"})
public class TestConfig {

    @Autowired
    private DataSource dataSource;

    @Value("${hbn.dialect}")
    private String dialect;

    @Value("${hbn.hbm2ddl}")
    private String hbmddl;


    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public Properties hibernateProperties() {

        Properties properties = new Properties();
        properties.put("hibernate.dialect", dialect);
        properties.put("hibernate.hbm2ddl.auto", hbmddl);
        properties.put("hibernate.format_sql", true);
        properties.put("hibernate.use_sql_comments", true);
        properties.put("hibernate.show_sql", true);

        return properties;

    }


    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {

         LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
         factoryBean.setDataSource(dataSource);
         factoryBean.setPackagesToScan("com.zantabri.auth_service");

         JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
         factoryBean.setJpaVendorAdapter(vendorAdapter);
         factoryBean.setJpaProperties(hibernateProperties());

         return factoryBean;

    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }



}
