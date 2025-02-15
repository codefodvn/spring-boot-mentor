package codefod.com.springbootmentor.config.datasource.demo2;

import codefod.com.springbootmentor.config.datasource.DatasourceUtil;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.Map;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "codefod.com.springbootmentor.repository.demo2",
        entityManagerFactoryRef = "demo2EntityManager",
        transactionManagerRef = "demo2TransactionManager")
public class Demo2DataSourceConfig {

    private final HikariDemo2DataSourceConfig hikariDemo2DataSourceConfig;
    private static final String SUFFIX_POOL_NAME = "_demo2";
    private static final String PACKAGE_ENTITY = "codefod.com.springbootmentor.entity.demo2";
    private static final String INSPECTOR = "codefod.com.springbootmentor.config.hibernate.SqlCommentStatementInspector";

    public Demo2DataSourceConfig(HikariDemo2DataSourceConfig hikariDemo2DataSourceConfig) {
        this.hikariDemo2DataSourceConfig = hikariDemo2DataSourceConfig;
    }

    @Bean(name = "demo2DataSource")
    public HikariDataSource adminHikariDataSource() {
        return DatasourceUtil.getHikariDataSource(hikariDemo2DataSourceConfig.getUsername(),
                hikariDemo2DataSourceConfig.getPassword(),
                hikariDemo2DataSourceConfig.getJdbcUrl(),
                hikariDemo2DataSourceConfig.getHikari().getMinimumIdle(),
                hikariDemo2DataSourceConfig.getHikari().getMaximumPoolSize(),
                hikariDemo2DataSourceConfig.getHikari().getIdleTimeout(),
                hikariDemo2DataSourceConfig.getHikari().getMaxLifetime(),
                hikariDemo2DataSourceConfig.getHikari().getConnectionTimeout(),
                hikariDemo2DataSourceConfig.getHikari().getPoolName(), SUFFIX_POOL_NAME);
    }

    @Bean(name = "demo2EntityManager")
    public LocalContainerEntityManagerFactoryBean demo2EntityManager(
            @Qualifier("demo2DataSource") HikariDataSource adminHikariDataSource) {
        Map<String, String> properties = new HashMap<>();
        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        properties.put("spring.jpa.hibernate.ddl-auto", "update");
        properties.put("spring.jpa.show-sql", "true");
        properties.put("hibernate.physical_naming_strategy", "codefod.com.springbootmentor.config.hibernate.CustomPhysicalNamingStrategy");
        properties.put("hibernate.session_factory.statement_inspector", INSPECTOR);

        return new LocalContainerEntityManagerFactoryBean() {{
            setDataSource(adminHikariDataSource);
            setPersistenceProviderClass(HibernatePersistenceProvider.class);
            setPersistenceUnitName("DEMO2_PERSISTENCE_UNIT");
            setPackagesToScan(PACKAGE_ENTITY);
            getJpaPropertyMap().putAll(properties);
        }};
    }

    @Bean(name = "demo2TransactionManager")
    public PlatformTransactionManager adminTransactionManager(
            @Qualifier("demo2EntityManager") EntityManagerFactory demo2EntityManager) {
        return new JpaTransactionManager(demo2EntityManager);
    }

}