package codefod.com.springbootmentor.config.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplateBuilder restTemplateBuilder() {
        return new RestTemplateBuilder();
    }

    @Bean
    public CodefodRestTemplate codefodRestTemplate(ObjectMapper objectMapper, RestTemplateBuilder builder) {
        RestTemplate restTemplate = createRestTemplate(builder, objectMapper);
        return new CodefodRestTemplate(restTemplate, objectMapper);
    }

    private RestTemplate createRestTemplate(RestTemplateBuilder builder, ObjectMapper objectMapper) {
        RestTemplate restTemplate = builder.build();
        var messageConverter = new MappingJackson2HttpMessageConverter();
        messageConverter.setPrettyPrint(false);
        messageConverter.setObjectMapper(objectMapper);
        messageConverter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
        restTemplate.getMessageConverters().removeIf(m -> m.getClass().isAssignableFrom(MappingJackson2HttpMessageConverter.class));
        restTemplate.getMessageConverters().add(messageConverter);
        return restTemplate;
    }
}
