package codefod.com.springbootmentor.config.log.filter;

import codefod.com.springbootmentor.config.log.cache.CachedBodyHttpServletRequest;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Lớp ContentCachingFilter này được sử dụng để chụp và lưu trữ nội dung yêu cầu HTTP,
 * cho phép xem và xử lý lại nội dung trong quá trình gỡ lỗi và giám sát ứng dụng.
 * Điều này có thể hữu ích khi cần phân tích và xem lại dữ liệu gửi đi và nhận về từ các yêu cầu HTTP.
 * **/
@Slf4j
public class ContentCachingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, @SuppressWarnings("NullableProblems") HttpServletResponse httpServletResponse,
                                    @SuppressWarnings("NullableProblems") FilterChain filterChain) throws ServletException, IOException {
        if (httpServletRequest.getContentType() != null && httpServletRequest.getContentType().contains("multipart/form-data")) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }
        CachedBodyHttpServletRequest cachedBodyHttpServletRequest = new CachedBodyHttpServletRequest(httpServletRequest);
        filterChain.doFilter(cachedBodyHttpServletRequest, httpServletResponse);
    }
}
