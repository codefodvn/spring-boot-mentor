package codefod.com.springbootmentor.config.datasource.demo1;

import codefod.com.springbootmentor.config.datasource.DatasourceUtil;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "codefod.com.springbootmentor.repository.demo1",
        entityManagerFactoryRef = "demo1EntityManager",
        transactionManagerRef = "demo1TransactionManager")
public class Demo1DataSourceConfig {

    private final HikariDemo1DataSourceConfig hikariDemo1DataSourceConfig;
    private static final String SUFFIX_POOL_NAME = "_demo1";
    private static final String PACKAGE_ENTITY = "codefod.com.springbootmentor.entity.demo1";
    private static final String INSPECTOR = "codefod.com.springbootmentor.config.hibernate.SqlCommentStatementInspector";

    public Demo1DataSourceConfig(HikariDemo1DataSourceConfig hikariDemo1DataSourceConfig) {
        this.hikariDemo1DataSourceConfig = hikariDemo1DataSourceConfig;
    }

    @Bean(name = "demo1DataSource")
    public HikariDataSource adminHikariDataSource() {
        return DatasourceUtil.getHikariDataSource(hikariDemo1DataSourceConfig.getUsername(),
                hikariDemo1DataSourceConfig.getPassword(),
                hikariDemo1DataSourceConfig.getJdbcUrl(),
                hikariDemo1DataSourceConfig.getHikari().getMinimumIdle(),
                hikariDemo1DataSourceConfig.getHikari().getMaximumPoolSize(),
                hikariDemo1DataSourceConfig.getHikari().getIdleTimeout(),
                hikariDemo1DataSourceConfig.getHikari().getMaxLifetime(),
                hikariDemo1DataSourceConfig.getHikari().getConnectionTimeout(),
                hikariDemo1DataSourceConfig.getHikari().getPoolName(), SUFFIX_POOL_NAME);
    }

    @Bean(name = "demo1EntityManager")
    public LocalContainerEntityManagerFactoryBean demo1EntityManager(
            @Qualifier("demo1DataSource") HikariDataSource adminHikariDataSource) {
        Map<String, String> properties = new HashMap<>();
        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        properties.put("spring.jpa.hibernate.ddl-auto", "update");
        properties.put("spring.jpa.show-sql", "true");
        properties.put("hibernate.physical_naming_strategy",
                "codefod.com.springbootmentor.config.hibernate.CustomPhysicalNamingStrategy");
        properties.put("hibernate.session_factory.statement_inspector", INSPECTOR);

        return new LocalContainerEntityManagerFactoryBean() {{
            setDataSource(adminHikariDataSource);
            setPersistenceProviderClass(HibernatePersistenceProvider.class);
            setPersistenceUnitName("DEMO1_PERSISTENCE_UNIT");
            setPackagesToScan(PACKAGE_ENTITY);
            getJpaPropertyMap().putAll(properties);
        }};
    }

    @Bean(name = "demo1TransactionManager")
    public PlatformTransactionManager adminTransactionManager(
            @Qualifier("demo1EntityManager") EntityManagerFactory demo1EntityManager) {
        return new JpaTransactionManager(demo1EntityManager);
    }

}