package codefod.com.springbootmentor.aop;

import io.github.bucket4j.Bucket;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Aspect
@Component
@Slf4j
public class RateLimitAspect {

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    // Lấy toàn bộ class có đuôi là Controller và có annotation CodefodRateLimit
    @Pointcut("(@annotation(codefodRateLimit) && execution(* *(..)) && within(*..*Controller))")
    public void rateLimitMethods(CodefodRateLimit codefodRateLimit) {
        // This method is just a placeholder for the pointcut expression.
    }


    @Around(value = "rateLimitMethods(codefodRateLimit)", argNames = "joinPoint,codefodRateLimit")
    public Object rateLimit(ProceedingJoinPoint joinPoint, CodefodRateLimit codefodRateLimit) throws Throwable {
        String key = generateKey(joinPoint);

        // Cấu hình bucket theo limit và period từ annotation
        int limit = codefodRateLimit.limit();
        int period = codefodRateLimit.period();

        log.info("Joint point", joinPoint.getSignature().getName());

        // Tạo bucket với thời gian refresh và giới hạn request
        Bucket bucket = buckets.computeIfAbsent(key, k -> createBucket(limit, period));

        // Kiểm tra xem request có được phép không
        if (bucket.tryConsume(1)) {
            return joinPoint.proceed();  // Cho phép request tiếp tục
        } else {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Too many requests, please try again later.");
        }
    }

    private Bucket createBucket(int token, int period) {
        return Bucket.builder()
                .addLimit(limit -> limit.capacity(1000)  // Giới hạn sức chứa của bucket là 50 tokens tại 1 thời điểm
                        .refillGreedy(token, Duration.ofSeconds(period)))  // Đổ đầy bucket với `token` tokens mỗi `period` giây
                .build();
    }


    private String generateKey(ProceedingJoinPoint joinPoint) {
        // Tạo một key duy nhất để phân biệt các phương thức (có thể sử dụng IP hoặc tên phương thức)
        return joinPoint.getSignature().toString();
    }
}

