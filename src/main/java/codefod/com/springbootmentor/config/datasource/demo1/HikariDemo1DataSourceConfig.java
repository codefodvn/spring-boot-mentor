package codefod.com.springbootmentor.config.datasource.demo1;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "spring.demo1.datasource")
@Validated
public class HikariDemo1DataSourceConfig {
    private String username;
    private String password;
    private String jdbcUrl;
    private HikariConfig hikari;

    @Getter
    @Setter
    public static class HikariConfig {
        @Value("${spring.finance.datasource.hikari.minimumIdle}")
        private Integer minimumIdle;
        @Value("${spring.finance.datasource.hikari.maximumPoolSize}")
        private Integer maximumPoolSize;
        private Integer idleTimeout = 30000;
        private Integer maxLifetime = 2000000;
        private Integer connectionTimeout = 30000;
        private String poolName = "HikariPoolSpringBootMentor";
    }
}

