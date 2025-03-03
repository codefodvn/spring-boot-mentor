package codefod.com.springbootmentor.config.log.advice;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import codefod.com.springbootmentor.common.constant.CustomHeaders;
import codefod.com.springbootmentor.common.constant.MDCConstant;
import codefod.com.springbootmentor.config.log.cache.CachedBodyHttpServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@Slf4j
@ControllerAdvice
public class LogResponseBodyAdviceAdapter implements ResponseBodyAdvice<Object> {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(
            "yyyyMMddHHmmss.SSS");

    public LogResponseBodyAdviceAdapter() {
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        OBJECT_MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public boolean supports(MethodParameter methodParameter,
                            Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public Object beforeBodyWrite(Object object, MethodParameter methodParameter,
                                  MediaType mediaType,
                                  Class<? extends HttpMessageConverter<?>> aClass,
                                  ServerHttpRequest serverHttpRequest,
                                  ServerHttpResponse serverHttpResponse) {
        if ((serverHttpRequest instanceof ServletServerHttpRequest serverHttpReq)
                && (serverHttpResponse instanceof ServletServerHttpResponse serverHttpRes)) {
            logResponse(serverHttpReq.getServletRequest(), serverHttpRes.getServletResponse(),
                    object);
        }

        return object;
    }

    public void logResponse(HttpServletRequest httpServletRequest,
                            HttpServletResponse httpServletResponse, Object body) {
        try {
            if (httpServletRequest.getRequestURI().startsWith("/actuator/")) {
                return;
            }

            String reqAt =
                    httpServletRequest.getAttribute(CachedBodyHttpServletRequest.REQUEST_AT) != null
                            ?
                            httpServletRequest.getAttribute(CachedBodyHttpServletRequest.REQUEST_AT)
                                    .toString() : "";

            MDC.put(MDCConstant.REQUEST_AT, reqAt);

            String resAt = DATE_TIME_FORMATTER.format(LocalDateTime.now());
            Object clientID = httpServletRequest.getAttribute("CLIENT_ID");

            MDC.put(MDCConstant.REQUEST_REQUEST_URI, httpServletRequest.getRequestURI());
            MDC.put(MDCConstant.TYPE, "api");

            if (!CollectionUtils.isEmpty(
                    httpServletResponse.getHeaders(CustomHeaders.X_B3_ERROR_CODE))) {
                MDC.put(MDCConstant.REQUEST_ERROR_CODE,
                        httpServletResponse.getHeaders(CustomHeaders.X_B3_ERROR_CODE).iterator()
                                .next());
            }

            MDC.put(MDCConstant.REQUEST_QUERY_STRING, httpServletRequest.getQueryString());
            MDC.put(MDCConstant.REQUEST_METHOD, httpServletRequest.getMethod());
            MDC.put(MDCConstant.REQUEST_CLIENT_ID, clientID != null ? clientID.toString() : "");

            if (StringUtils.isNotEmpty(resAt) && StringUtils.isNotEmpty(reqAt)) {
                MDC.put(MDCConstant.REQUEST_TIME,
                        String.valueOf(Double.parseDouble(resAt) - Double.parseDouble(reqAt)));
            }

            String reqBody =
                    httpServletRequest.getAttribute(CachedBodyHttpServletRequest.REQUEST_BODY_CACHE)
                            != null ?
                            httpServletRequest.getAttribute(
                                    CachedBodyHttpServletRequest.REQUEST_BODY_CACHE).toString()
                            : "";
            String reqSize =
                    httpServletRequest.getAttribute(CachedBodyHttpServletRequest.REQUEST_SIZE)
                            != null ?
                            httpServletRequest.getAttribute(
                                    CachedBodyHttpServletRequest.REQUEST_SIZE).toString() : "";

            MDC.put(MDCConstant.REQUEST_SIZE, reqSize);

            int length = body != null && StringUtils.isNotEmpty(body.toString()) ? body.toString()
                    .getBytes().length : 0;

            MDC.put(MDCConstant.REQUEST_RESPONSE_SIZE, String.valueOf(length));

            MsgLog msgLog = new MsgLog();
            msgLog.setRequestBody(reqBody);
            msgLog.setResponseBody(body);

            MDC.put(MDCConstant.REQUEST_RESPONSE_HTTP_STATUS,
                    String.valueOf(httpServletResponse.getStatus()));
            MDC.put(MDCConstant.REQUEST_RESPONSE_AT, resAt);

            if (httpServletResponse.getStatus() == 401) {
                log.error("error verify token{} ;__ {}",
                        httpServletRequest.getHeader("Authorization"),
                        httpServletRequest.getRequestURI());
            }

            log.info(OBJECT_MAPPER.writeValueAsString(msgLog));

            MDC.remove(MDCConstant.TYPE);
        } catch (Exception ex) {
            log.error("error log msg request", ex);
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class MsgLog {

        private Map<String, String> requestHeader;
        private Object requestBody;
        private Map<String, String> responseHeader;
        private Object responseBody;

        @Override
        public String toString() {
            return "MsgLog{" +
                    "request_header=" + requestHeader +
                    ", request_body='" + requestBody + '\'' +
                    ", response_header=" + responseHeader +
                    ", response_body='" + responseBody + '\'' +
                    '}';
        }
    }

}
