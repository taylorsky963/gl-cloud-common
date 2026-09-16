package com.gl.myparadise.common.elasticsearch.base;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.*;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
@RequiredArgsConstructor
public class EsClient {

    @NonNull
    private RestHighLevelClient client;

    public SearchResponse search(SearchRequest request, RequestOptions options) throws IOException {
        if (request.source() != null) {
            request.source().trackTotalHits(true);
            // 每次 search 一条，高频任务下单服务每小时上万条，只在排查时才需要，故用 debug
            log.debug("Auto-enabled trackTotalHits for request: {}", Arrays.toString(request.indices()));
        }
        SearchResponse response = client.search(request, options);
        warnIfAggregationsMissing(request, response);
        return response;
    }

    /**
     * 请求带了聚合、却因「目标索引全不存在」拿不到 aggregations 时告警。
     *
     * <p>ES 在目标索引一个都不存在时（配合 ignoreUnavailable / allowNoIndices 的 lenient 选项不报错）
     * 返回 {@code _shards.total=0} 且响应里<b>没有 aggregations 节点</b>，{@code getAggregations()} 为 null；
     * 而只要有 ≥1 个目标索引存在（即使查询 0 命中）就一定有 aggregations（0 桶）。</p>
     *
     * <p>调用方若直接 {@code getAggregations().get(name)} 链式取值会 NPE，且异常常被上层
     * catch 成「无数据」，把真实原因（索引缺失）盖掉。这里统一打一条带索引名的告警便于定位。
     * 本方法只做日志、不改变返回值与任何行为。</p>
     */
    private void warnIfAggregationsMissing(SearchRequest request, SearchResponse response) {
        try {
            if (response == null || request.source() == null || request.source().aggregations() == null) {
                return;
            }
            if (response.getAggregations() == null) {
                log.warn("⚠️ ES 请求了聚合但响应无 aggregations（勿直接链式取聚合，请判空）: totalShards={}, indices={}"
                                + (response.getTotalShards() == 0 ? "，原因：目标索引全不存在" : ""),
                        response.getTotalShards(), Arrays.toString(request.indices()));
            }
        } catch (Exception e) {
            // 纯诊断日志，任何异常都不允许影响查询结果
            log.debug("ES aggregations 缺失检查异常", e);
        }
    }

    public IndexResponse index(IndexRequest request, RequestOptions options) throws IOException {
        return client.index(request, options);
    }

    public UpdateResponse update(UpdateRequest request, RequestOptions options) throws IOException {
        return client.update(request, options);
    }

    public DeleteResponse delete(DeleteRequest request, RequestOptions options) throws IOException {
        return client.delete(request, options);
    }

    public BulkResponse bulk(BulkRequest request, RequestOptions options) throws IOException {
        return client.bulk(request, options);
    }

    public BulkByScrollResponse deleteByQuery(DeleteByQueryRequest request, RequestOptions options) throws IOException {
        return client.deleteByQuery(request, options);
    }

    public SearchResponse scroll(SearchScrollRequest request, RequestOptions options) throws IOException {
        return client.scroll(request, options);
    }

    public ClearScrollResponse clearScroll(ClearScrollRequest request, RequestOptions options) throws IOException {
        return client.clearScroll(request, options);
    }

    public void close() {
        try {
            client.close();
        } catch (IOException e) {
            log.error("Failed to close RestHighLevelClient", e);
        }
    }
}
