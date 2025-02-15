package codefod.com.springbootmentor.config.datasource;


import com.zaxxer.hikari.HikariDataSource;

public class DatasourceUtil {

    public static HikariDataSource getHikariDataSource(String username, String password, String jdbcUrl, Integer minimumIdle,
                                                       Integer maximumPoolSize, Integer idleTimeout, Integer maxLifetime,
                                                       Integer connectionTimeout, String poolName, String suffixPoolName) {
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setUsername(username);
        hikariDataSource.setPassword(password);
        hikariDataSource.setJdbcUrl(jdbcUrl);
        hikariDataSource.setMinimumIdle(minimumIdle);
        hikariDataSource.setMaximumPoolSize(maximumPoolSize);
        hikariDataSource.setIdleTimeout(idleTimeout);
        hikariDataSource.setMaxLifetime(maxLifetime);
        hikariDataSource.setConnectionTimeout(connectionTimeout);
        hikariDataSource.setPoolName(poolName + suffixPoolName);
        return hikariDataSource;
    }
}
