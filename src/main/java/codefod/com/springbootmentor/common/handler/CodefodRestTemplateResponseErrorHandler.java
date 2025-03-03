package codefod.com.springbootmentor.common.handler;

import codefod.com.springbootmentor.common.exception.RestErrorException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

@Slf4j
public class CodefodRestTemplateResponseErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode().isError();
    }


    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        String body = "";

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.getBody(), StandardCharsets.UTF_8))) {
            body = reader.lines().collect(Collectors.joining(""));
        }

        log.error("Error response {}: {}", response.getStatusCode().value(), body);

        throw new RestErrorException(response.getStatusCode().value(), body);
    }
}