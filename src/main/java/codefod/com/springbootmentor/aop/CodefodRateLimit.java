package codefod.com.springbootmentor.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CodefodRateLimit {
    int limit() default 5;  // Giới hạn số lần request
    int period() default 60; // Thời gian giới hạn trong giây (default 60 giây)
}
