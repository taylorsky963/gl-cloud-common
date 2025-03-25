package com.gl.myparadise.common.elasticsearch.base;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public abstract class ElasticsearchBaseDao {

    public final static String USER = "gl_user";
    public final static String BETTING = "report-betting-";
    public final static String GAME_TRANSFER = "report-game-transfer-";
    public final static String MEMBER_VIP = "report-member-vip-";
    public final static String DAILY_USER_BETTING = "report-daily-user-betting-";

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

    @SuppressWarnings("unchecked")
    public <V> V getValFromSearchHit(SearchHit hitField, String fieldName, V defaultVal) {
        return getValFromSearchHit(hitField, fieldName, defaultVal, defaultVal != null ? (Class<V>) Objects.requireNonNull(defaultVal).getClass() : null);
    }

    public <V> V getValFromSearchHit(SearchHit hitField, String fieldName, V defaultVal, Class<V> type) {
        if (!StringUtils.hasText(fieldName) || hitField == null || !hitField.hasSource()) {
            return defaultVal;
        }
        Map<String, Object> sourceAsMap = hitField.getSourceAsMap();
        Object value = getValueFromNestedMap(sourceAsMap, fieldName, defaultVal);
        return convertValue(value, defaultVal, type);
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
    private <V> V convertValue(Object value, V defaultVal, Class<V> type) {
        if (value == null) return defaultVal;

        Class<?> targetClass = (type != null) ? type : (defaultVal != null ? defaultVal.getClass() : null);
        if (targetClass == null) return (V) value;

        try {
            if (targetClass == String.class) {
                return (V) value.toString();
            } else if (targetClass == BigDecimal.class && value instanceof Number) { // 新增 BigDecimal 支持
                return (V) new BigDecimal(value.toString());
            } else if (Number.class.isAssignableFrom(targetClass) && value instanceof Number) {
                return (V) convertNumber((Number) value, targetClass);
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