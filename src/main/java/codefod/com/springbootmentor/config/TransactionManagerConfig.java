package codefod.com.springbootmentor.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@SuppressWarnings("all")
@Configuration
public class TransactionManagerConfig {

    @Bean
    @Primary
    public ChainedTransactionManager chainedTransactionManager(
            @Qualifier("demo1TransactionManager") PlatformTransactionManager demo1TransactionManager,
            @Qualifier("demo2TransactionManager") PlatformTransactionManager demo2TransactionManager) {
        return new ChainedTransactionManager(demo1TransactionManager, demo2TransactionManager);
    }
}