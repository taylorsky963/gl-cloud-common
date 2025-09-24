package com.gl.myparadise.common.tools.http;


import javax.servlet.http.HttpServletRequest;

/**
 * @author Vincent
 * @description:
 * @className: RequestUtil
 * @projectName: gl-cloud-base
 * @date 2025-09-22 - 22:53
 */
public class HttpRequestUtil {

    /**
     * 获取用户真实访问的域名（不带端口）
     */
    public static String getOriginalDomain(HttpServletRequest request, boolean isPort) {

        // 优先取 Nginx 转发头
        String host = request.getHeader("X-Forwarded-Host");

        if (host == null || host.isEmpty()) {
            // 如果没有 Nginx，取标准 Host 头
            host = request.getHeader("Host");

        }

        if (host == null || host.isEmpty()) {
            // 最后兜底：从 request 获取
            host = request.getServerName();
        }

        if(isPort){

            // 去掉端口（如果有）
            if (host != null && host.contains(":")) {

                host = host.split(":")[0];

            }

        }

        return host;
    }
}
