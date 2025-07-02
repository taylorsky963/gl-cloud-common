package com.gl.myparadise.common.webclient.util;

import com.gl.myparadise.common.tools.jason.GJonUtil;
import com.gl.myparadise.common.webclient.model.response.HttpResponseErrorData;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * @author Vincent
 * @description:
 * @className: WebClientErrorHandler
 * @projectName: gl-cloud-base
 * @date 2025-04-01 - 21:54
 */
@Slf4j
public class WebClientErrorHandler {

    public static Mono<String> handle(Throwable e) {

        String msg = e.getMessage();

        log.error("🌐 WebClient 调用异常: {}", msg, e);

        if (msg != null && msg.trim().startsWith("{") && msg.trim().endsWith("}")) {
            // ✅ 已是 JSON 结构
            return Mono.just(msg);
        }

        // ❗构建标准错误结构
        HttpResponseErrorData error = new HttpResponseErrorData();

        error.setResponseErrorCode("500");

        error.setResponseErrorMessages(msg != null ? msg : "未知错误");

        return Mono.just(GJonUtil.toJson(error));
    }
}
