package org.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import com.zaxxer.hikari.HikariDataSource;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@ComponentScan(value = "org.example")
@EnableTransactionManagement
@EnableJpaRepositories("org.example.dao")
@PropertySource("classpath:database.properties")
public class GeneralAppConfig {

    private final String PROPERTY_DRIVER = "driver";
    private final String PROPERTY_URL = "url";
    private final String PROPERTY_USERNAME = "user";
    private final String PROPERTY_PASSWORD = "password";
    private final String PROPERTY_SHOW_SQL = "hibernate.show_sql";
    private final String PROPERTY_DIALECT = "hibernate.dialect";
    private final String HBM2DDL = "hibernate.hbm2ddl";

    @Autowired
    Environment environment;

    @Bean
    public LocalSessionFactoryBean entityManagerFactory() {
        LocalSessionFactoryBean lfb = new LocalSessionFactoryBean();
        lfb.setDataSource(dataSource());
        lfb.setPackagesToScan("org.example.domain");
        lfb.setHibernateProperties(hibernateProps());
        return lfb;
    }

    @Bean
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(environment.getProperty(PROPERTY_URL));
        dataSource.setUsername(environment.getProperty(PROPERTY_USERNAME));
        dataSource.setPassword(environment.getProperty(PROPERTY_PASSWORD));
        dataSource.setDriverClassName(environment.getProperty(PROPERTY_DRIVER));
        dataSource.setMinimumIdle(5);
        return dataSource;
    }

    public Properties hibernateProps() {
        Properties properties = new Properties();
        properties.setProperty(PROPERTY_DIALECT, environment.getProperty(PROPERTY_DIALECT));
        properties.setProperty(PROPERTY_SHOW_SQL, environment.getProperty(PROPERTY_SHOW_SQL));
        properties.setProperty(HBM2DDL, environment.getProperty(HBM2DDL));
        return properties;
    }

    @Bean
    public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }
}
