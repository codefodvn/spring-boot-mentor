package codefod.com.springbootmentor.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import codefod.com.springbootmentor.common.constant.ErrorEnum;
import codefod.com.springbootmentor.common.exception.CodefodException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    private static ObjectMapper createObjectMapper(PropertyNamingStrategy strategy) {
        ObjectMapper mapper = objectMapper.copy();
        mapper.setPropertyNamingStrategy(strategy);
        return mapper;
    }

    public static String toJson(Object obj, PropertyNamingStrategy namingStrategy) {
        try {
            ObjectMapper mapper = createObjectMapper(namingStrategy);
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("Error converting object to JSON: {}", e.getMessage());
            throw new CodefodException(ErrorEnum.INTERNAL_SERVER_ERROR);
        }
    }

    public static <T> T fromJson(String json, Class<T> returnType,
                                 PropertyNamingStrategy namingStrategy) {
        try {
            ObjectMapper mapper = createObjectMapper(namingStrategy);
            return mapper.readValue(json, returnType);
        } catch (JsonProcessingException e) {
            log.error("Error deserializing JSON with Class<T>: {}", e.getMessage());
            throw new CodefodException(ErrorEnum.INTERNAL_SERVER_ERROR);
        }
    }

    public static <T> T fromJson(String json, TypeReference<T> valueTypeRef,
                                 PropertyNamingStrategy namingStrategy) {
        try {
            ObjectMapper mapper = createObjectMapper(namingStrategy);
            return mapper.readValue(json, valueTypeRef);
        } catch (JsonProcessingException e) {
            log.error("Error deserializing JSON with TypeReference<T>: {}", e.getMessage());
            throw new CodefodException(ErrorEnum.INTERNAL_SERVER_ERROR);
        }
    }

    public static String toJson(Object obj) {
        return toJsonCamelCase(obj);
    }

    public static String toJsonSnackCase(Object obj) {
        return toJson(obj, PropertyNamingStrategies.SNAKE_CASE);
    }

    public static <T> T fromJsonSnackCase(String json, Class<T> returnType) {
        return fromJson(json, returnType, PropertyNamingStrategies.SNAKE_CASE);
    }

    public static <T> T fromJsonSnackCase(String json, TypeReference<T> valueTypeRef) {
        return fromJson(json, valueTypeRef, PropertyNamingStrategies.SNAKE_CASE);
    }

    public static String toJsonCamelCase(Object obj) {
        return toJson(obj, PropertyNamingStrategies.LOWER_CAMEL_CASE);
    }

    public static <T> T fromJsonCamelCase(String json, Class<T> returnType) {
        return fromJson(json, returnType, PropertyNamingStrategies.LOWER_CAMEL_CASE);
    }

    public static <T> T fromJsonCamelCase(String json, TypeReference<T> valueTypeRef) {
        return fromJson(json, valueTypeRef, PropertyNamingStrategies.LOWER_CAMEL_CASE);
    }

    public static String toJsonUpperCamelCase(Object obj) {
        return toJson(obj, PropertyNamingStrategies.UPPER_CAMEL_CASE);
    }

    public static <T> T fromJsonUpperCamelCase(String json, Class<T> returnType) {
        return fromJson(json, returnType, PropertyNamingStrategies.UPPER_CAMEL_CASE);
    }

    public static <T> T fromJsonUpperCamelCase(String json, TypeReference<T> valueTypeRef) {
        return fromJson(json, valueTypeRef, PropertyNamingStrategies.UPPER_CAMEL_CASE);
    }
}
