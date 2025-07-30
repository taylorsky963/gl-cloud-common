package com.gl.myparadise.common.webclient.service.option.impl;

import com.gl.myparadise.common.tools.jason.GJonUtil;
import com.gl.myparadise.common.webclient.config.HttpPoolClientRetryProperties;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    public Future<String> sendRequestPostHttpServer(WebClientRequestModel webClientRequestModel) {

        log.debug("sendRequestPostHttpServer start is : " + webClientRequestModel.getPath());

        log.debug("sendRequestPostHttpServer data is : " + webClientRequestModel.getData());

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

                log.error("sendRequestPostHttpServer error, path: {}, data: {}", webClientRequestModel.getPath(), webClientRequestModel.getData(), e);

                return e.getMessage();

            } finally {

                log.debug("sendRequestPostHttpServer end : {}", webClientRequestModel.getPath());

            }

        });

    }

    public Future<String> sendRequestPostFormHttpServer(WebClientRequestModel webClientRequestModel) {

        log.debug("sendRequestPostFormHttpServer start is : " + webClientRequestModel.getPath());

        log.debug("sendRequestPostFormHttpServer data is : " + webClientRequestModel.getData());

        return getExecutorService(webClientRequestModel.getThreadPoolBeanName()).submit(() -> {

            try {

                return getWebClient(webClientRequestModel.getWebClientBeanName())
                        .post()
                        .uri(webClientRequestModel.getPath())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED) // 修改为表单提交
                        .headers(buildHttpHeaders(webClientRequestModel.getHttpHeaderMap()))
                        .acceptCharset(StandardCharsets.UTF_8)
                        .body(BodyInserters.fromFormData(buildMultiValueMap(webClientRequestModel.getDataMap()))) // 使用表单数据
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
                        .retryWhen(getClientRetry(webClientRequestModel.getWebClientBeanName()))
                        .block();

            } catch (Exception e) {

                log.error("sendRequestPostFormHttpServer error, path: {}, data: {}",
                        webClientRequestModel.getPath(), webClientRequestModel.getData(), e);

                return e.getMessage();

            } finally {

                log.debug("sendRequestPostFormHttpServer end : {}", webClientRequestModel.getPath());

            }
        });
    }

    @Override
    public Future<String> sendRequestPutHttpServer(WebClientRequestModel webClientRequestModel) {

        log.debug("sendRequestPutHttpServer start is : " + webClientRequestModel.getPath());

        log.debug("sendRequestPutHttpServer data is : " + webClientRequestModel.getData());

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

                log.error("sendRequestPutHttpServer data is : " + webClientRequestModel.getData());

                log.error("sendRequestPutHttpServer is error path : " + webClientRequestModel.getPath(), e);

                return e.getMessage();

            } finally {

                log.debug("sendRequestPutHttpServer end : " + webClientRequestModel.getPath());

            }

        });

    }

    @Override
    public Future<String> sendRequestGetMultiValueMapHttpServer(WebClientRequestModel webClientRequestModel) {

        log.debug("sendRequestGetMultiValueMapHttpServer start is : " + webClientRequestModel.getPath());

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

                log.error("sendRequestGetMultiValueMapHttpServer data is : " + GJonUtil.toJson(webClientRequestModel.getDataMap()));

                log.error("sendRequestGetMultiValueMapHttpServer is error path : " + webClientRequestModel.getPath(), e);

                return e.getMessage();

            } finally {

                log.debug("sendRequestGetMultiValueMapHttpServer end : " + webClientRequestModel.getPath());

            }

        });

    }

    @Override
    public Future<String> sendRequestGetHttpServer(WebClientRequestModel webClientRequestModel) {

        log.debug("sendRequestGetHttpServer start is : " + webClientRequestModel.getPath());

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

                log.error("sendRequestGetHttpServer data is : " + GJonUtil.toJson(webClientRequestModel.getDataMap()));

                log.error("sendRequestGetHttpServer is error path : " + webClientRequestModel.getPath(), e);

                return e.getMessage();

            } finally {

                log.debug("sendRequestGetHttpServer end : " + webClientRequestModel.getPath());

            }

        });

    }

    @Override
    public Future<String> sendRequestGetHttpNoNoneMatchServer(WebClientRequestModel webClientRequestModel) {

        log.debug("sendRequestGetHttpServer start is : " + webClientRequestModel.getPath());

        return getExecutorService(webClientRequestModel.getThreadPoolBeanName()).submit(() -> {

            try {

                return getWebClient(webClientRequestModel.getWebClientBeanName()).get().uri(uriBuilder -> uriBuilder
                                .path(webClientRequestModel.getPath())
                                .build(webClientRequestModel.getDataMap()))
                        .headers(buildHttpHeaders(webClientRequestModel.getHttpHeaderMap()))
                        .acceptCharset(StandardCharsets.UTF_8)
//                        .ifModifiedSince(ZonedDateTime.now())
//                        .ifNoneMatch("*")
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

                log.error("sendRequestGetHttpServer data is : " + GJonUtil.toJson(webClientRequestModel.getDataMap()));

                log.error("sendRequestGetHttpServer is error path : " + webClientRequestModel.getPath(), e);

                return e.getMessage();

            } finally {

                log.debug("sendRequestGetHttpServer end : " + webClientRequestModel.getPath());

            }

        });

    }
    /**
     * @param webClientRequestModel
     * @param multipartFile
     * @return
     */
    @Override
    public Future<String> sendRequestStreamUploadHttpServer(WebClientRequestModel webClientRequestModel, MultipartFile multipartFile) {

        log.debug("sendRequestStreamUploadHttpServer start : " + webClientRequestModel.getPath());

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

                log.error("sendRequestStreamUploadHttpServer is error path : " + webClientRequestModel.getPath(), e);

                return e.getMessage();

            } finally {

                log.debug("sendRequestStreamUploadHttpServer end : " + webClientRequestModel.getPath());

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
    public Future<StreamDownLoadResponseData> sendRequestStreamDownLoadHttpServer(WebClientStreamRequestModel webClientStreamRequestModel) {

        log.debug("sendRequestStreamDownLoadHttpServer start is : " + webClientStreamRequestModel.getPath());

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

                log.error("sendRequestStreamDownLoadHttpServer is error mediaName : " + webClientStreamRequestModel.getMediaName(), e);

                log.error("sendRequestStreamDownLoadHttpServer is error path : " + webClientStreamRequestModel.getPath(), e);

                streamDownLoadResponseData.setResponseErrorMessages(e.getMessage());

                return streamDownLoadResponseData;

            } finally {

                log.debug("sendRequestStreamDownLoadHttpServer end : " + webClientStreamRequestModel.getPath());

            }

        });

    }


    @Override
    public Future<String> sendRequestDeleteHttpServer(WebClientRequestModel webClientRequestModel) {

        log.debug("sendRequestDeleteHttpServer start is : " + webClientRequestModel.getPath());

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

                log.error("sendRequestDeleteHttpServer is error path : " + webClientRequestModel.getPath(), e);

                return e.getMessage();

            } finally {

                log.debug("sendRequestDeleteHttpServer end : " + webClientRequestModel.getPath());

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

        log.error("sendRequestPostHttpServer response is error responseCode is : " + response.statusCode());

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