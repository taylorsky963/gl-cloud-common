package com.gl.myparadise.common.elasticsearch.configuration;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Configuration
public class RestHighLevelClientConfiguration {

    @Value("#{'${spring.elasticsearch.rest.uris}'.split(',')}")
    private List<String> server;

    @Bean(destroyMethod = "close")
    @Qualifier
    public RestHighLevelClient esClient() {
        HttpHost[] hosts = Optional.ofNullable(server)
                .orElse(Collections.emptyList())
                .stream().map(HttpHost::create)
                .toArray(HttpHost[]::new);

        return new RestHighLevelClient(RestClient.builder(hosts));
    }

}