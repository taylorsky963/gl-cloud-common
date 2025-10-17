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

@Slf4j
@RequiredArgsConstructor
public class EsClient {

    @NonNull
    private RestHighLevelClient client;

    public SearchResponse search(SearchRequest request, RequestOptions options) throws IOException {
        if (request.source() != null) {
            request.source().trackTotalHits(true);
            log.info("✅ Auto-enabled trackTotalHits for request: {}", request.indices());
        }
        return client.search(request, options);
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
