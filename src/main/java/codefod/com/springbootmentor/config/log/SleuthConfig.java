package codefod.com.springbootmentor.config.log;

import brave.baggage.BaggageField;
import brave.baggage.CorrelationScopeConfig;
import brave.context.slf4j.MDCScopeDecorator;
import brave.propagation.CurrentTraceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * Cấu hình này cho phép bạn gắn mã lỗi vào MDC và theo dõi mã lỗi này trong quá trình truyền tải
 * giữa các yêu cầu trong hệ thống phân tán. Bằng cách tích hợp Sleuth với MDC, bạn có thể dễ dàng
 * theo dõi và phân tích thông tin trong quá trình gỡ lỗi và giám sát hệ thống.
 **/
@Configuration
public class SleuthConfig {

    @Bean
    BaggageField errorCodeField() {
        return BaggageField.create("errorCode");
    }

    @Bean
    CurrentTraceContext.ScopeDecorator mdcScopeDecorator() {
        return MDCScopeDecorator.newBuilder()
                .clear()
                .add(CorrelationScopeConfig.SingleCorrelationField.newBuilder(errorCodeField())
                        .flushOnUpdate()
                        .build())
                .build();
    }
}