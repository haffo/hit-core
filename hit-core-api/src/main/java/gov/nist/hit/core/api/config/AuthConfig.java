/**
 * This software was developed at the National Institute of Standards and Technology by employees of
 * the Federal Government in the course of their official duties. Pursuant to title 17 Section 105
 * of the United States Code this software is not subject to copyright protection and is in the
 * public domain. This is an experimental system. NIST assumes no responsibility whatsoever for its
 * use by other parties, and makes no guarantees, expressed or implied, about its quality,
 * reliability, or any other characteristic. We would appreciate acknowledgement if the software is
 * used. This software can be redistributed and/or modified freely provided that any derivative
 * works bear some notice that they are derived from it, and any modified versions bear some notice
 * that they have been modified.
 */
package gov.nist.hit.core.api.config;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author Harold Affo (harold.affo@nist.gov) Apr 7, 2015
 */
@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
@EnableJpaRepositories(value = "gov.nist.auth.hit.core.repo", entityManagerFactoryRef = "authEMF",
    transactionManagerRef = "authTransactionManager")
@PropertySource(value = {"classpath:app-auth-config.properties"})
@ImportResource({"classpath:app-security-config.xml"})
public class AuthConfig {

  @Autowired
  private Environment env;


  @Bean(name = "authEMF")
  public org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean entityManagerFactory(
      @Qualifier("authJpaVA") JpaVendorAdapter jpaVendorAdapter,
      @Qualifier("authDataSource") DataSource authDataSource) {
    LocalContainerEntityManagerFactoryBean lef = new LocalContainerEntityManagerFactoryBean();
    lef.setDataSource(authDataSource);
    lef.setPersistenceUnitName(env.getProperty("auth.jpa.persistenceUnitName"));
    lef.setJpaVendorAdapter(jpaVendorAdapter);
    lef.setJpaProperties(jpaProperties());
    lef.setPackagesToScan("gov.nist.auth.hit.core.domain");
    lef.setLoadTimeWeaver(new InstrumentationLoadTimeWeaver());
    return lef;
  }

  @Bean(name = "authJpaVA")
  public JpaVendorAdapter jpaVendorAdapter() {
    HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
    jpaVendorAdapter.setShowSql(Boolean.getBoolean(env.getProperty("auth.jpa.showSql")));
    jpaVendorAdapter.setGenerateDdl(Boolean.getBoolean(env.getProperty("auth.jpa.generateDdl")));
    jpaVendorAdapter.setDatabase(Database.valueOf(env.getProperty("auth.jpa.database")));
    jpaVendorAdapter.setDatabasePlatform(env.getProperty("auth.jpa.databasePlatform"));
    return jpaVendorAdapter;
  }

  private Properties jpaProperties() {
    Properties properties = new Properties();
    properties.put("hibernate.hbm2ddl.auto", env.getProperty("auth.hibernate.hbm2ddl.auto"));
    properties.put("hibernate.globally_quoted_identifiers",
        env.getProperty("auth.hibernate.globally_quoted_identifiers"));
    return properties;
  }

  @Bean(name = "authTransactionManager")
  public PlatformTransactionManager transactionManager(
      @Qualifier("authEMF") EntityManagerFactory entityManagerFactory) {
    JpaTransactionManager transactionManager = new JpaTransactionManager();
    transactionManager.setEntityManagerFactory(entityManagerFactory);
    transactionManager.setJpaDialect(new HibernateJpaDialect());
    return transactionManager;
  }

  @Bean
  public JavaMailSenderImpl mailSender() {
    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
    mailSender.setHost(env.getProperty("mail.host"));
    mailSender.setPort(Integer.valueOf(env.getProperty("mail.port")));
    mailSender.setProtocol(env.getProperty("mail.protocol"));
    Properties javaMailProperties = new Properties();
    javaMailProperties.setProperty("mail.smtp.auth", env.getProperty("mail.auth"));
    javaMailProperties.setProperty("mail.debug", env.getProperty("mail.debug"));
    mailSender.setJavaMailProperties(javaMailProperties);
    return mailSender;
  }

  @Bean
  public org.springframework.mail.SimpleMailMessage templateMessage() {
    org.springframework.mail.SimpleMailMessage templateMessage =
        new org.springframework.mail.SimpleMailMessage();
    templateMessage.setFrom(env.getProperty("mail.from"));
    templateMessage.setSubject(env.getProperty("mail.subject"));
    return templateMessage;
  }

  @Bean
  public DataSourceInitializer dataSourceInitializer(
      @Qualifier("authDataSource") DataSource authDataSource) {
    ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator();
    resourceDatabasePopulator.addScript(new ClassPathResource("/app-auth-schema.sql"));
    DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
    dataSourceInitializer.setDataSource(authDataSource);
    dataSourceInitializer.setDatabasePopulator(resourceDatabasePopulator);
    return dataSourceInitializer;
  }


}
