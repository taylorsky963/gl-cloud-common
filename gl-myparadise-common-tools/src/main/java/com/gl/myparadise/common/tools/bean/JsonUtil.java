package com.gl.myparadise.common.tools.bean;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class JsonUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.setSerializationInclusion(Include.NON_NULL);
    }

    @SneakyThrows
    public static <T> List<T> toListBean(String content, Class<T> clz) {
        if (StringUtils.isBlank(content) || clz == null) {
            throw new IllegalArgumentException("content is required.");
        }
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        return objectMapper.readValue(content, typeFactory.constructCollectionType(List.class, clz));
    }

    @SneakyThrows
    public static String toJson(Object obj) {
        if (obj == null) {
            return null;
        }
        return objectMapper.writeValueAsString(obj);
    }

    @SneakyThrows
    public static <T> T toBean(String content, Class<T> clz) {
        if (StringUtils.isBlank(content) || clz == null) {
            return null;
        }
        return objectMapper.readValue(content, clz);
    }
}
