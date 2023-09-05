package com.kokio.productapi.config;

import com.zaxxer.hikari.HikariDataSource;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;


@Configuration
@EnableJpaRepositories(
    basePackages = "com.kokio.entitymodule.domain.product.repository",
    entityManagerFactoryRef = "productEntityManagerFactory",
    transactionManagerRef = "productTransactionManager")
public class ProductDataSourceConfig {

  @Bean(name = "productDataSource")
  @ConfigurationProperties(prefix = "spring.datasource.product")
  public DataSource dataSource() {
    return DataSourceBuilder.create().type(HikariDataSource.class).build();
  }

  @Bean(name = "productEntityManagerFactory")
  public LocalContainerEntityManagerFactoryBean entityManagerFactory(
      EntityManagerFactoryBuilder builder, @Qualifier("productDataSource") DataSource dataSource) {

    Map<String, Object> properties = new HashMap<>();
    properties.put("hibernate.hbm2ddl.auto", "create-drop");
    properties.put("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
    properties.put("hibernate.show_sql", true);
    properties.put("spring.jpa.hibernate.naming.physical-strategy",
        "org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl");
    properties.put("pring.jpa.hibernate.naming.implicit-strategy",
        "org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy");
    return builder.dataSource(dataSource)
        .properties(properties)
        .packages("com.kokio.entitymodule.domain.product.entity")
        .persistenceUnit("product").build();
  }


  @Bean(name = "productTransactionManager")
  public PlatformTransactionManager userTransactionManager(
      @Qualifier("productEntityManagerFactory") EntityManagerFactory entityManagerFactory) {

    return new JpaTransactionManager(entityManagerFactory);

  }
}