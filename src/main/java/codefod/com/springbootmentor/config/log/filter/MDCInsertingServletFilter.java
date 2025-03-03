package codefod.com.springbootmentor.config.log.filter;

import codefod.com.springbootmentor.common.constant.MDCConstant;
import codefod.com.springbootmentor.common.util.CommonUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

public class MDCInsertingServletFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(@SuppressWarnings("NullableProblems") HttpServletRequest request,
                                    @SuppressWarnings("NullableProblems") HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        insertIntoMDC(request);
        try {
            filterChain.doFilter(request, response);
        } finally {
            clearMDC();
        }
    }

    void insertIntoMDC(HttpServletRequest request) {
        MDC.put(MDCConstant.CLIENT_IP, CommonUtil.getClientIp(request));
    }

    void clearMDC() {
        MDC.clear();
    }

}