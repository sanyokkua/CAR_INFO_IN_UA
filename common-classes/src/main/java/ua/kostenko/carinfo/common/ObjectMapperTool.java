package ua.kostenko.carinfo.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
@Deprecated
public final class ObjectMapperTool {
    private final static ObjectMapper objectMapper = new ObjectMapper();

    @Deprecated
    public static String writeValueAsString(Object object) {
        Preconditions.checkNotNull(object, "Argument can't be null");
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("Error with creating String from Object");
        }
        return null;
    }

    @Deprecated
    public static <T> T readValue(String content, Class<T> valueType) {
        try {
            return objectMapper.readValue(content, valueType);
        } catch (IOException e) {
            log.error("Problem with mapping json string to object", e);
        }
        return null;
    }
}
