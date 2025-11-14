package com.gl.myparadise.common.elasticsearch.configuration;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Configuration
@Slf4j
public class RestHighLevelClientConfiguration {

    @Value("#{'${spring.elasticsearch.rest.uris}'.split(',')}")
    private List<String> server;

    @Value("${spring.elasticsearch.rest.username:els}")
    private String username;

    @Value("${spring.elasticsearch.rest.password:}")
    private String password;

    private static HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
        httpClientBuilder.addInterceptorFirst((HttpRequestInterceptor) (request, context) -> search(request));
        return httpClientBuilder;
    }

    private static void search(HttpRequest request) {
//        if ("/_cluster/health/".equals(request.getRequestLine().getUri())) {
//            return;
//        }
//
//        String url = Optional.of(request.getRequestLine().getUri())
//                .map(data -> StringUtils.split(data, "?"))
//                .map(data -> data[0])
//                .orElse(request.getRequestLine().getUri());
//
//        if (request instanceof HttpEntityEnclosingRequest) {
//            HttpEntityEnclosingRequest entityRequest = (HttpEntityEnclosingRequest) request;
//            try {
//                if (entityRequest.getEntity() != null) {
//                    String requestBody = EntityUtils.toString(entityRequest.getEntity());
//                    log.info("Elasticsearch Request - URI: {}, Query: {}", url, requestBody);
//                }
//            } catch (IOException e) {
//                log.error("Failed to parse Elasticsearch request body", e);
//            }
//        } else {
//            log.info("Elasticsearch Request - URI: {}, Query: {}", url, "No body");
//        }
    }

    @Bean(destroyMethod = "close")
    @Qualifier
    public RestHighLevelClient esClient() {
        HttpHost[] hosts = Optional.ofNullable(server)
                .orElse(Collections.emptyList())
                .stream().map(HttpHost::create)
                .toArray(HttpHost[]::new);

        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        if (StringUtils.hasText(username) && StringUtils.hasText(password)) {
            log.info("Elasticsearch credentials provided, using them for authentication. username: {}", username);
            credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
        } else {
            log.warn("Elasticsearch credentials not provided, proceeding without authentication. This may cause access issues.");
        }

        RestClientBuilder builder = RestClient.builder(hosts)
                .setHttpClientConfigCallback(httpClientBuilder ->
                        customizeHttpClient(httpClientBuilder)
                                .setDefaultCredentialsProvider(credentialsProvider));

        return new RestHighLevelClient(builder);
    }

}