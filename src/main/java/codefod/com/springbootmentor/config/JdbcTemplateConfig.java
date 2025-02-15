package codefod.com.springbootmentor.config;

import codefod.com.springbootmentor.config.jpa.CustomNamedParameterJdbcTemplate;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class JdbcTemplateConfig {

    @Bean
    @Primary
    NamedParameterJdbcTemplate demo1JdbcTemplate(
            @Qualifier("demo1DataSource") DataSource dataSource) {
        return new CustomNamedParameterJdbcTemplate(dataSource);
    }

    @Bean
    NamedParameterJdbcTemplate demo2JdbcTemplate(
            @Qualifier("demo2DataSource") DataSource dataSource) {
        return new CustomNamedParameterJdbcTemplate(dataSource);
    }
}
