package codefod.com.springbootmentor.config.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import codefod.com.springbootmentor.common.constant.ErrorEnum;
import codefod.com.springbootmentor.common.exception.CodefodException;
import codefod.com.springbootmentor.common.util.JsonUtil;
import io.netty.channel.ConnectTimeoutException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RequiredArgsConstructor
public class CodefodRestTemplate {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;


    public <T> ResponseEntity<T> send(String url, HttpMethod httpMethod, HttpEntity<?> httpEntity,
                                      Map<String, String> params, Class<T> typeParameterClass) {
        try {
            logRequest(url, httpMethod, httpEntity, params);
        } catch (JsonProcessingException e) {
            throw new CodefodException(e.getMessage());
        }
        ResponseEntity<T> responseEntity = restTemplate.exchange(url, httpMethod, httpEntity,
                typeParameterClass, params);
        try {
            logResponse(responseEntity);
        } catch (JsonProcessingException e) {
            throw new CodefodException(e.getMessage());
        }

        return responseEntity;
    }

    public <T> ResponseEntity<T> sendGet(String url, HttpHeaders headers,
                                         Map<String, String> params, Class<T> typeParameterClass) {
        HttpEntity<?> httpEntity = new HttpEntity<>(null, headers);
        if (!CollectionUtils.isEmpty(params)) {
            List<String> list = new ArrayList<>();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(value)) {
                    list.add(key + "=" + value);
                }
            }

            if (!CollectionUtils.isEmpty(list)) {
                url += "?" + String.join("&", list);
            }
        }

        return send(url, HttpMethod.GET, httpEntity, new HashMap<>(), typeParameterClass);
    }

    public <T> ResponseEntity<T> sendPost(String url, HttpHeaders headers,
                                          Map<String, String> params, Object body,
                                          Class<T> typeParameterClass) {
        HttpEntity<?> httpEntity = new HttpEntity<>(body, headers);
        return send(url, HttpMethod.POST, httpEntity, params, typeParameterClass);

    }

    public <T> ResponseEntity<T> sendPost(String url, HttpHeaders headers, Object body,
                                          Class<T> typeParameterClass) {
        return sendPost(url, headers, new HashMap<>(), body, typeParameterClass);
    }

    public <T> ResponseEntity<T> exchange(HttpMethod method, String url,
                                          @Nullable HttpEntity<?> requestEntity,
                                          Class<T> responseType,
                                          boolean logRequestBody, boolean logResponseBody) {
        Map<String, ?> uriVariables = new HashMap<>();

        log.info("HTTP call: {} {}", method.name(), url);

        if (logRequestBody && requestEntity != null) {
            logRequest(requestEntity);
        }

        try {
            ResponseEntity<T> ret = restTemplate.exchange(url, method, requestEntity, responseType,
                    uriVariables);

            if (logResponseBody) {
                logResponse(ret);
            }
            return ret;
        } catch (ResourceAccessException e) {
            if (e.getCause() instanceof ConnectTimeoutException) {
                log.error("ConnectTimeoutException: ", e);
                throw new CodefodException(ErrorEnum.INTERNAL_SERVER_ERROR);
            } else if (e.getCause() instanceof SocketTimeoutException) {
                log.error("SocketTimeoutException: ", e);
                throw new CodefodException(ErrorEnum.INTERNAL_SERVER_ERROR);
            } else {
                throw e;
            }
        } catch (JsonProcessingException e) {
            log.error("JsonProcessingException: ", e);
            throw new CodefodException(ErrorEnum.INTERNAL_SERVER_ERROR);
        }
    }

    private void logRequest(String url, HttpMethod httpMethod, HttpEntity<?> httpEntity,
                            Map<String, String> params) throws JsonProcessingException {
        List<String> param = new ArrayList<>();

        if (!CollectionUtils.isEmpty(params)) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                param.add(key + "=" + value);
            }
        }

        String queryString = String.join("&", param);

        log.info("{}: {}?{}", httpMethod.name(), url, queryString);
        if (httpMethod.name().equalsIgnoreCase("POST")) {
            logRequest(httpEntity);
        }
    }

    private static void logRequest(HttpEntity<?> requestEntity) {
        log.info("Request body: {}", JsonUtil.toJson(requestEntity.getBody()));
    }

    private <T> void logResponse(ResponseEntity<T> responseEntity) throws JsonProcessingException {
        log.info("Response body: {}", objectMapper.writeValueAsString(responseEntity.getBody()));
    }
}
