package codefod.com.springbootmentor.util;

import java.security.SecureRandom;
import java.util.Base64;
import org.slf4j.MDC;

public class LogUtil {

    private LogUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static void generateCorrelationId() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = new byte[20];
        secureRandom.nextBytes(randomBytes);
        String correlationId = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
        MDC.put("correlationId", correlationId);
    }

    public static String getCorrelationId() {
        return MDC.get("correlationId");
    }
}
