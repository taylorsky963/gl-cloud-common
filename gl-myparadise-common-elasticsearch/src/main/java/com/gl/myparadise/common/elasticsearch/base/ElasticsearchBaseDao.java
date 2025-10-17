package com.gl.myparadise.common.elasticsearch.base;

import lombok.SneakyThrows;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.metrics.Cardinality;
import org.elasticsearch.search.aggregations.metrics.Sum;
import org.elasticsearch.search.aggregations.metrics.ValueCount;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.util.*;


public abstract class ElasticsearchBaseDao {

    public final static String USER = "gl_user";
    public final static String BETTING = "report-betting-20";
    public final static String GAME_TRANSFER = "report-game-transfer-20";
    public final static String MEMBER_VIP = "report-member-vip-20";
    public final static String DAILY_USER_BETTING = "report-daily-user-betting-20";
    public final static String DAILY_USER_RECHARGE = "report-daily-user-recharge-20";
    public final static String RECHARGE = "report-recharge-20";
    public final static String WITHDRAW = "report-withdraw-20";
    public final static String POINT_CHANGE = "report-point-change-20";
    public final static String BALANCE_CHANGE = "report-balance-change-20";
    public final static String BETFLOW_CHANGE = "report-bet-flow-change-20";
    public final static String ACTIVITY_MEMBER_AWARD = "report-activity-member-award-20";
    public final static String SEAMLESS_GAME_TRANSACTION = "report-seamless-game-transaction-20";
    public final static String MEMBER_LOGIN_LOG = "report-member-login-log-20";

    public ZoneOffset timeZone = ZoneOffset.ofHours(8);

    @Autowired
    private RestHighLevelClient rawEsClient;

    public EsClient esClient;

    @PostConstruct
    public void init() {
        this.esClient = new EsClient(rawEsClient);
    }

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
            } else if (Timestamp.class.isAssignableFrom(targetClass) && value instanceof Long) {
                return (V) new Timestamp((long) value);
            } if (Date.class.isAssignableFrom(targetClass) && value instanceof Long) {
                return (V) new Date((long) value);
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

    @SuppressWarnings("unchecked")
    public <V> V getValFromAggregation(Aggregations aggregations, String aggName, V defaultVal, Class<V> type) {
        if (aggregations == null || !StringUtils.hasText(aggName)) {
            return defaultVal;
        }

        Aggregation aggregation = aggregations.get(aggName);
        if (aggregation == null) {
            return defaultVal;
        }

        try {
            if (type == null && defaultVal != null) {
                type = (Class<V>) defaultVal.getClass();
            }

            if (type == null) {
                return defaultVal; // 無法確定類型，返回預設值
            }

            if (aggregation instanceof Sum) {
                double value = ((Sum) aggregation).getValue();
                if (type == BigDecimal.class) {
                    return (V) new BigDecimal(String.valueOf(value));
                } else if (type == Double.class) {
                    return (V) Double.valueOf(value);
                } else if (type == Integer.class) {
                    return (V) Integer.valueOf((int) value);
                } else if (type == Long.class) {
                    return (V) Long.valueOf((long) value);
                }
            } else if (aggregation instanceof ValueCount) {
                long value = ((ValueCount) aggregation).getValue();
                if (type == Integer.class) {
                    return (V) Integer.valueOf((int) value);
                } else if (type == Long.class) {
                    return (V) Long.valueOf(value);
                } else if (type == BigDecimal.class) {
                    return (V) new BigDecimal(String.valueOf(value));
                }
            }else if (aggregation instanceof Cardinality) {
                long value = ((Cardinality) aggregation).getValue();
                if (type == Integer.class) {
                    return (V) Integer.valueOf((int) value);
                } else if (type == Long.class) {
                    return (V) Long.valueOf(value);
                } else if (type == BigDecimal.class) {
                    return (V) new BigDecimal(String.valueOf(value));
                }
            }

            return defaultVal;
        } catch (Exception e) {
            return defaultVal;
        }
    }

    /**
     * 批量查询某个索引，支持 terms 分批查询
     *
     * @param indexName     索引名
     * @param fieldName     terms 查询字段名，例如 "memberInfoId"
     * @param ids           要查询的 ID 集合
     * @param includeFields 需要返回的字段名数组
     * @param batchSize     每批查询数量（建议 1000~5000）
     * @return 所有命中结果的 Map 列表（每个 Map 是一条记录的字段键值）
     */
    @SneakyThrows
    public List<Map<String, Object>> batchTermsQuery(
            String indexName,
            String fieldName,
            List<?> ids,
            String[] includeFields,
            int batchSize) {
        List<Map<String, Object>> results = new ArrayList<>();
        for (int i = 0; i < ids.size(); i += batchSize) {
            List<?> batch = ids.subList(i, Math.min(i + batchSize, ids.size()));
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder()
                    .query(QueryBuilders.termsQuery(fieldName, batch))
                    .fetchSource(includeFields, null)
                    .size(batchSize);
            SearchRequest searchRequest = new SearchRequest(indexName).source(sourceBuilder);
            SearchResponse response = esClient.search(searchRequest, RequestOptions.DEFAULT);
            for (SearchHit hit : response.getHits()) {
                results.add(hit.getSourceAsMap());
            }
        }
        return results;
    }

}