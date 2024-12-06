package com.gl.myparadise.common.mvc.Interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: BaseWebFormInterceptor
 * @Date 2020-06-15 - 16:11
 */
public class BaseWebFormInterceptor extends HandlerInterceptorAdapter {

    private static final Logger logger = LoggerFactory.getLogger(BaseWebFormInterceptor.class);

    public static final String MYBASE_PATH = "mybasePath";

    public static final String MYBASE_RESOURCEPATH = "myResourcePath";

    public static final String MYBASE_SERVERNAME = "myServerName";

    @Value("${spring.webform.staticresource.servername:127.0.0.1}")
    private String resourceScheme;

    @Value("${spring.webform.base.servername:127.0.0.1}")
    private String serverBaseName;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        String path = request.getContextPath();

        String scheme = request.getScheme();

        String serverName = request.getServerName();

        if(resourceScheme == null){

            resourceScheme = serverName;

        }

        if(serverBaseName == null){

            serverBaseName = serverName;

        }

        int port = request.getServerPort();

        String mybasePath = scheme + "://" + serverName + ":" + port + path;

        String resourcePath = scheme + "://" + resourceScheme ;

        String myServerName = scheme + "://" + serverBaseName ;

        request.setAttribute(MYBASE_PATH, mybasePath);

        request.setAttribute(MYBASE_RESOURCEPATH, resourcePath);

        request.setAttribute(MYBASE_SERVERNAME, myServerName);

        logger.debug("BaseWebFormInterceptor mybasePath : " + mybasePath);

        logger.debug("BaseWebFormInterceptor myServerName : " + myServerName);

        logger.debug("BaseWebFormInterceptor resourcePath : " + resourcePath);

        return true;
    }

}
