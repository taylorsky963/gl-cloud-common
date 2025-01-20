package com.gl.myparadise.common.elasticsearch.base;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.time.ZoneOffset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public abstract class ElasticsearchBaseDao {

    public final static String USER = "gl_user";
    public final static String BETTING = "report-betting-2*";

    public ZoneOffset timeZone = ZoneOffset.ofHours(8);

    @Autowired
    public RestHighLevelClient esClient;

    public boolean success(SearchResponse response) {
        if (response.status().getStatus() != 200) {
            return false;
        }

        SearchHits hits = response.getHits();

        if (hits.getHits() == null) {
            return false;
        }

        if (hits.getTotalHits() == null) {
            return false;
        }

        return hits.getTotalHits().value != 0;
    }

    public <V> V getValFromSearchHit(SearchHit hitField, String fieldName, V defaultVal) {
        if (!StringUtils.hasText(fieldName) || hitField == null || !hitField.hasSource()) {
            return defaultVal;
        }
        Map<String, Object> sourceAsMap = hitField.getSourceAsMap();
        Object value = getValueFromNestedMap(sourceAsMap, fieldName, defaultVal);

        return convertValue(value, defaultVal);
    }

    private Object getValueFromNestedMap(Map<String, Object> sourceMap, String fieldName, Object defaultVal) {
        String[] split = fieldName.split("\\.");
        Map<String, Object> currentMap = sourceMap;

        for (int i = 0; i < split.length - 1; i++) {
            Object nextMap = currentMap.get(split[i]);
            if (nextMap instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> tempMap = (Map<String, Object>) nextMap;
                currentMap = tempMap;
            } else {
                return defaultVal;
            }
        }
        return currentMap.getOrDefault(split[split.length - 1], defaultVal);
    }

    @SuppressWarnings("unchecked")
    private <V> V convertValue(Object value, V defaultVal) {
        if (value == null) return defaultVal;
        if (defaultVal == null) return (V) value;

        try {
            if (defaultVal instanceof String) {
                return (V) value.toString();
            } else if (defaultVal instanceof Number && value instanceof Number) {
                return (V) convertNumber((Number) value, defaultVal.getClass());
            }
            return (V) value;
        } catch (ClassCastException e) {
            return defaultVal;
        }
    }

    private Number convertNumber(Number value, Class<?> targetClass) {
        if (targetClass == Integer.class) return value.intValue();
        if (targetClass == Long.class) return value.longValue();
        if (targetClass == Double.class) return value.doubleValue();
        if (targetClass == Float.class) return value.floatValue();
        if (targetClass == Short.class) return value.shortValue();
        if (targetClass == Byte.class) return value.byteValue();
        return value;
    }
}