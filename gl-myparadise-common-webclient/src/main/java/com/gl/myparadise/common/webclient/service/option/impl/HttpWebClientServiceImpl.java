package com.gl.myparadise.common.webclient.service.option.impl;

import com.gl.myparadise.common.tools.jason.GJonUtil;
import com.gl.myparadise.common.webclient.config.HttpPoolClientRetryProperties;
import com.gl.myparadise.common.webclient.config.HttpPoolConfigurationProperties;
import com.gl.myparadise.common.webclient.constants.WebClientConstants;
import com.gl.myparadise.common.webclient.model.request.WebClientRequestModel;
import com.gl.myparadise.common.webclient.model.request.stream.WebClientStreamRequestModel;
import com.gl.myparadise.common.webclient.model.response.HttpResponseErrorData;
import com.gl.myparadise.common.webclient.model.response.stream.StreamDownLoadResponseData;
import com.gl.myparadise.common.webclient.service.option.HttpWebClientService;
import com.gl.myparadise.common.webclient.util.HttpMediaDataUtils;
import io.netty.channel.ConnectTimeoutException;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.function.Consumer;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: MatrixConnectServiceImpl
 * @ProjectName: gi-matrix-parent
 * @Date 2023-06-24 - 5:38 a.m.
 */
@Service
@Slf4j
public class HttpWebClientServiceImpl implements HttpWebClientService {

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public Future<String> sendRequestPostMatrixServer(WebClientRequestModel webClientRequestModel) {

        log.debug("sendRequestPostMatrixServer start is : " + webClientRequestModel.getPath());

        log.debug("sendRequestPostMatrixServer data is : " + webClientRequestModel.getData());

        return getExecutorService(webClientRequestModel.getThreadPoolBeanName()).submit(() -> {

            try {

                return getWebClient(webClientRequestModel.getWebClientBeanName())
                        .post().uri(webClientRequestModel.getPath())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(buildHttpHeaders(webClientRequestModel.getHttpHeaderMap()))
                        .acceptCharset(StandardCharsets.UTF_8)
                        .body(BodyInserters.fromObject(webClientRequestModel.getData()))
                        .ifModifiedSince(ZonedDateTime.now())
                        .ifNoneMatch("*")
                        .retrieve()
                        .onStatus(HttpStatus::is5xxServerError, response -> {

                            return bulidResponseError(response);

                        })
                        .onStatus(HttpStatus::is4xxClientError, response -> {

                            return bulidResponseError(response);

                        })
                        .bodyToMono(String.class)
                        .retryWhen(getClientRetry(webClientRequestModel.getWebClientBeanName())).block();

            } catch (Exception e) {

                log.error("sendRequestPostMatrixServer error, path: {}, data: {}", webClientRequestModel.getPath(), webClientRequestModel.getData(), e);

                return e.getMessage();

            } finally {

                log.debug("sendRequestPostMatrixServer end : {}", webClientRequestModel.getPath());

            }

        });

    }

    @Override
    public Future<String> sendRequestPutMatrixServer(WebClientRequestModel webClientRequestModel) {

        log.debug("sendRequestPutMatrixServer start is : " + webClientRequestModel.getPath());

        log.debug("sendRequestPutMatrixServer data is : " + webClientRequestModel.getData());

        return getExecutorService(webClientRequestModel.getThreadPoolBeanName()).submit(() -> {

            try {

                return getWebClient(webClientRequestModel.getWebClientBeanName())
                        .put().uri(webClientRequestModel.getPath())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(buildHttpHeaders(webClientRequestModel.getHttpHeaderMap()))
                        .acceptCharset(StandardCharsets.UTF_8)
                        .body(BodyInserters.fromObject(webClientRequestModel.getData()))
                        .ifModifiedSince(ZonedDateTime.now())
                        .ifNoneMatch("*")
                        .retrieve()
                        .onStatus(HttpStatus::is5xxServerError, response -> {

                            return bulidResponseError(response);

                        })
                        .onStatus(HttpStatus::is4xxClientError, response -> {

                            return bulidResponseError(response);

                        })
                        .bodyToMono(String.class)
                        .retryWhen(getClientRetry(webClientRequestModel.getWebClientBeanName())).block();

            } catch (Exception e) {

                log.error("sendRequestPutMatrixServer data is : " + webClientRequestModel.getData());

                log.error("sendRequestPutMatrixServer is error path : " + webClientRequestModel.getPath(), e);

                return e.getMessage();

            } finally {

                log.debug("sendRequestPutMatrixServer end : " + webClientRequestModel.getPath());

            }

        });

    }

    @Override
    public Future<String> sendRequestGetMultiValueMapMatrixServer(WebClientRequestModel webClientRequestModel) {

        log.debug("sendRequestGetMultiValueMapMatrixServer start is : " + webClientRequestModel.getPath());

        return getExecutorService(webClientRequestModel.getThreadPoolBeanName()).submit(() -> {

            try {

                return getWebClient(webClientRequestModel.getWebClientBeanName()).get().uri(uriBuilder -> uriBuilder
                                .path(webClientRequestModel.getPath())
                                .queryParams(buildMultiValueMap(webClientRequestModel.getDataMap()))
                                .build())
                        .headers(buildHttpHeaders(webClientRequestModel.getHttpHeaderMap()))
                        .acceptCharset(StandardCharsets.UTF_8)
                        .ifModifiedSince(ZonedDateTime.now())
                        .ifNoneMatch("*")
                        .retrieve()
                        .onStatus(HttpStatus::is5xxServerError, response -> {

                            return bulidResponseError(response);

                        })
                        .onStatus(HttpStatus::is4xxClientError, response -> {

                            return bulidResponseError(response);

                        })
                        .bodyToMono(String.class)
                        .retryWhen(getClientRetry(webClientRequestModel.getWebClientBeanName())).block();

            } catch (Exception e) {

                log.error("sendRequestGetMultiValueMapMatrixServer data is : " + GJonUtil.toJson(webClientRequestModel.getDataMap()));

                log.error("sendRequestGetMultiValueMapMatrixServer is error path : " + webClientRequestModel.getPath(), e);

                return e.getMessage();

            } finally {

                log.debug("sendRequestGetMultiValueMapMatrixServer end : " + webClientRequestModel.getPath());

            }

        });

    }

    @Override
    public Future<String> sendRequestGetMatrixServer(WebClientRequestModel webClientRequestModel) {

        log.debug("sendRequestGetMatrixServer start is : " + webClientRequestModel.getPath());

        return getExecutorService(webClientRequestModel.getThreadPoolBeanName()).submit(() -> {

            try {

                return getWebClient(webClientRequestModel.getWebClientBeanName()).get().uri(uriBuilder -> uriBuilder
                                .path(webClientRequestModel.getPath())
                                .build(webClientRequestModel.getDataMap()))
                        .headers(buildHttpHeaders(webClientRequestModel.getHttpHeaderMap()))
                        .acceptCharset(StandardCharsets.UTF_8)
                        .ifModifiedSince(ZonedDateTime.now())
                        .ifNoneMatch("*")
                        .retrieve()
                        .onStatus(HttpStatus::is5xxServerError, response -> {

                            return bulidResponseError(response);

                        })
                        .onStatus(HttpStatus::is4xxClientError, response -> {

                            return bulidResponseError(response);

                        })
                        .bodyToMono(String.class)
                        .retryWhen(getClientRetry(webClientRequestModel.getWebClientBeanName())).block();

            } catch (Exception e) {

                log.error("sendRequestGetMatrixServer data is : " + GJonUtil.toJson(webClientRequestModel.getDataMap()));

                log.error("sendRequestGetMatrixServer is error path : " + webClientRequestModel.getPath(), e);

                return e.getMessage();

            } finally {

                log.debug("sendRequestGetMatrixServer end : " + webClientRequestModel.getPath());

            }

        });

    }

    /**
     * @param webClientRequestModel
     * @param multipartFile
     * @return
     */
    @Override
    public Future<String> sendRequestStreamUploadMatrixServer(WebClientRequestModel webClientRequestModel, MultipartFile multipartFile) {

        log.debug("sendRequestStreamUploadMatrixServer start : " + webClientRequestModel.getPath());

        return getExecutorService(webClientRequestModel.getThreadPoolBeanName()).submit(() -> {

            try {

                final HashMap<String, String> map = new HashMap<>();

                map.put("filename", multipartFile.getName());

                return getWebClient(webClientRequestModel.getWebClientBeanName()).post().uri(uriBuilder -> uriBuilder
                                .path(webClientRequestModel.getPath())
                                .build(map))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.valueOf(multipartFile.getContentType()))
                        .headers(buildHttpHeaders(webClientRequestModel.getHttpHeaderMap()))
                        .contentLength(multipartFile.getSize())
                        .acceptCharset(StandardCharsets.UTF_8)
                        .bodyValue(multipartFile.getBytes())
                        .ifModifiedSince(ZonedDateTime.now())
                        .ifNoneMatch("*")
                        .retrieve()
                        .onStatus(HttpStatus::is5xxServerError, response -> {

                            return bulidResponseError(response);

                        })
                        .onStatus(HttpStatus::is4xxClientError, response -> {

                            return bulidResponseError(response);

                        })
                        .bodyToMono(String.class)
                        .retryWhen(getClientRetry(webClientRequestModel.getWebClientBeanName())).block();

            } catch (Exception e) {

                log.error("sendRequestStreamUploadMatrixServer is error path : " + webClientRequestModel.getPath(), e);

                return e.getMessage();

            } finally {

                log.debug("sendRequestStreamUploadMatrixServer end : " + webClientRequestModel.getPath());

            }

        });

    }

    /**
     * 下载文件
     *
     * @param webClientStreamRequestModel
     * @return
     */
    @Override
    public Future<StreamDownLoadResponseData> sendRequestStreamDownLoadMatrixServer(WebClientStreamRequestModel webClientStreamRequestModel) {

        log.debug("sendRequestStreamDownLoadMatrixServer start is : " + webClientStreamRequestModel.getPath());

        return getExecutorService(webClientStreamRequestModel.getThreadPoolBeanName()).submit(() -> {

            Resource resource = null;

            StreamDownLoadResponseData streamDownLoadResponseData = new StreamDownLoadResponseData();

            try {

                resource = getWebClient(webClientStreamRequestModel.getWebClientBeanName()).get().uri(uriBuilder -> uriBuilder
                                .path(webClientStreamRequestModel.getPath())
                                .build())
                        .accept(HttpMediaDataUtils.getMediaType(webClientStreamRequestModel.getMediaName()))
                        .headers(buildHttpHeaders(webClientStreamRequestModel.getHttpHeaderMap()))
                        .acceptCharset(StandardCharsets.UTF_8)
                        .ifModifiedSince(ZonedDateTime.now())
                        .ifNoneMatch("*")
                        .retrieve()
                        .onStatus(HttpStatus::is5xxServerError, response -> {

                            return bulidResponseError(response);

                        })
                        .onStatus(HttpStatus::is4xxClientError, response -> {

                            return bulidResponseError(response);

                        })
                        .bodyToMono(Resource.class)
                        .retryWhen(getClientRetry(webClientStreamRequestModel.getWebClientBeanName())).block();

                if (resource != null) {

                    streamDownLoadResponseData.setStreamData(toByteArray(resource.getInputStream()));

                }

                return streamDownLoadResponseData;


            } catch (Exception e) {

                log.error("sendRequestStreamDownLoadMatrixServer is error mediaName : " + webClientStreamRequestModel.getMediaName(), e);

                log.error("sendRequestStreamDownLoadMatrixServer is error path : " + webClientStreamRequestModel.getPath(), e);

                streamDownLoadResponseData.setResponseErrorMessages(e.getMessage());

                return streamDownLoadResponseData;

            } finally {

                log.debug("sendRequestStreamDownLoadMatrixServer end : " + webClientStreamRequestModel.getPath());

            }

        });

    }


    @Override
    public Future<String> sendRequestDeleteMatrixServer(WebClientRequestModel webClientRequestModel) {

        log.debug("sendRequestDeleteMatrixServer start is : " + webClientRequestModel.getPath());

        return getExecutorService(webClientRequestModel.getThreadPoolBeanName()).submit(() -> {

            try {

                return getWebClient(webClientRequestModel.getWebClientBeanName())
                        .delete().uri(webClientRequestModel.getPath())
                        .accept(MediaType.APPLICATION_JSON)
                        .headers(buildHttpHeaders(webClientRequestModel.getHttpHeaderMap()))
                        .acceptCharset(StandardCharsets.UTF_8)
                        .ifModifiedSince(ZonedDateTime.now())
                        .ifNoneMatch("*")
                        .retrieve()
                        .onStatus(HttpStatus::is5xxServerError, response -> {

                            return bulidResponseError(response);

                        })
                        .onStatus(HttpStatus::is4xxClientError, response -> {

                            return bulidResponseError(response);

                        })
                        .bodyToMono(String.class)
                        .retryWhen(getClientRetry(webClientRequestModel.getWebClientBeanName())).block();

            } catch (Exception e) {

                log.error("sendRequestDeleteMatrixServer is error path : " + webClientRequestModel.getPath(), e);

                return e.getMessage();

            } finally {

                log.debug("sendRequestDeleteMatrixServer end : " + webClientRequestModel.getPath());

            }

        });

    }

    /**
     * 统一的错误处理类
     *
     * @param response
     * @return
     */
    private Mono<RuntimeException> bulidResponseError(ClientResponse response) {

        log.error("sendRequestPostMatrixServer response is error responseCode is : " + response.statusCode());

        HttpResponseErrorData HttpResponseErrorData = new HttpResponseErrorData();

        HttpResponseErrorData.setResponseErrorCode(Integer.toString(response.statusCode().value()));

        HttpResponseErrorData.setResponseErrorMessages(response.statusCode().name());

        return Mono.just(new RuntimeException(GJonUtil.toJson(HttpResponseErrorData)));

    }

    /**
     * 获取http client 重试对象
     *
     * @return
     */
    private Retry getClientRetry(String webClientBeanName) {

        HttpPoolClientRetryProperties httpPoolClientRetryProperties = WebClientConstants.httpPoolRetryPropertiesMap.get(webClientBeanName);

        return Retry.backoff(
                        httpPoolClientRetryProperties.getRetryTimes(),
                        Duration.ofSeconds(httpPoolClientRetryProperties.getRetryInitialInterval()))
                .maxBackoff(Duration.ofSeconds(httpPoolClientRetryProperties.getRetryMaxInterval()))
                .jitter(0.5) // 可选，避免雪崩
                .filter(throwable -> throwable instanceof ConnectTimeoutException)
                .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> retrySignal.failure());
    }

    private Consumer<HttpHeaders> buildHttpHeaders(Map<String, String> headerMap) {

        Consumer<HttpHeaders> headersConsumer = httpHeaders -> {

            for (Map.Entry<String, String> entry : headerMap.entrySet()) {

                httpHeaders.add(entry.getKey(), entry.getValue());

            }
        };

        return headersConsumer;

    }

    private MultiValueMap buildMultiValueMap(Map<String, Object> dataMap) {

        MultiValueMap<String, Object> params = new LinkedMultiValueMap();

        if (dataMap != null && !dataMap.isEmpty()) {

            dataMap.forEach((key, value) -> {

                if (StringUtils.isNotEmpty((String) value)) {

                    params.add(key, value);

                }

            });

        }

        return params;
    }

    public static byte[] toByteArray(InputStream input) throws IOException {

        ByteArrayOutputStream output = new ByteArrayOutputStream();

        byte[] buffer = new byte[4096];

        int n = 0;

        while (-1 != (n = input.read(buffer))) {

            output.write(buffer, 0, n);

        }

        return output.toByteArray();
    }

    private WebClient getWebClient(String webClientBeanName) {

        return applicationContext.getBean(webClientBeanName, WebClient.class);
    }

    private ExecutorService getExecutorService(String threadPoolBeanName) {

        return applicationContext.getBean(threadPoolBeanName, ExecutorService.class);
    }


}