package com.gl.myparadise.common.mvc.Interceptor;


import com.gl.myparadise.common.tools.datetime.DateTimeUtil;
import com.gl.myparadise.common.tools.http.IpAddressUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: GLAPITimeInterceptor
 * @ProjectName: gl-matrix-parent
 * @Date 2023-10-09 - 15:14
 */
public class GLWebAPITimeInterceptor extends HandlerInterceptorAdapter {

    private Logger logger = LoggerFactory.getLogger(GLWebAPITimeInterceptor.class);

    @Value("${web.check.url.run.time:5.0}")
    private double checkUrlRumTime;

    @Value("${web.check.url.noPrint:query-syncdata-notice,query-syncdata}")
    private String[] noPrintUrlArray;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if ((handler instanceof HandlerMethod)) {

            request.setAttribute("api_startTime", System.currentTimeMillis());

            request.setAttribute("api_real_ip", IpAddressUtil.getRealIP(request));

            return true;

        }

        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        // 请求结束时间

        Long endTime = System.currentTimeMillis();
        // 从HttpServletRequest获取开始时间

        Long startTime = (Long) request.getAttribute("api_startTime");
        // 打印接口信息及耗时

        try {

            printLongTimeUrl(request, startTime, endTime);

        } catch (Exception e) {

            logger.error("GLAPITimeInterceptor afterCompletion printLongTimeUrl error", e);

        }

    }


    /**
     * 获取完整的URL路径
     *
     * @param request 请求对象{@link HttpServletRequest}
     * @return 完整的URL路径
     */
    private String getFullUrl(HttpServletRequest request) {

        try {
            //记录请求参数
            StringBuilder sb = new StringBuilder();
            String method = request.getMethod();
            sb.append(method).append(" ");
            sb.append(request.getRequestURL().toString());
            if (RequestMethod.POST.name().equals(method)) {
                //获取参数
                Map<String, String[]> pm = request.getParameterMap();

                if (!pm.isEmpty()) {

                    Set<Map.Entry<String, String[]>> es = pm.entrySet();
                    Iterator<Map.Entry<String, String[]>> iterator = es.iterator();
                    int pointer = 0;
                    while (iterator.hasNext()) {
                        if (pointer == 0) {
                            sb.append("?");
                        } else {
                            sb.append("&");
                        }
                        Map.Entry<String, String[]> next = iterator.next();
                        String key = next.getKey();
                        String[] value = next.getValue();
                        for (int i = 0; i < value.length; i++) {
                            if (i != 0) {
                                sb.append("&");
                            }
                            if (value[i].length() <= 20) {
                                sb.append(key).append("=").append(value[i]);
                            } else {
                                sb.append(key).append("=").append(value[i].substring(1, 20)).append("…");
                            }
                        }
                        pointer++;
                    }

                }

            }
            return sb.toString();

        } catch (Exception e) {

            logger.error("getFullUrl is error", e);
        }

        return "";

    }

    private void printLongTimeUrl(HttpServletRequest request, Long startTime, Long endTime) {

        String dateFormatStr = "yyyy-MM-dd HH:mm:ss.SSS";

        String endTimeStr = DateTimeUtil.formatDateToString(new Date(endTime), dateFormatStr);

        String starTimeStr = DateTimeUtil.formatDateToString(new Date(startTime), dateFormatStr);

        String requestUrl = getFullUrl(request);

        double runTime = ((endTime - startTime) * 1.000) / 1000;

        if (StringUtils.isNotEmpty(requestUrl) && runTime > checkUrlRumTime) {

            if (!checkNoPrintUrl(requestUrl)) {

                logger.error("{} {}；开始时间{} -- 结束时间{} : 耗时：{}s",
                        request.getAttribute("api_real_ip"), requestUrl, starTimeStr, endTimeStr, runTime);

            }

        }

    }

    private boolean checkNoPrintUrl(String requestUrl) {

        boolean isNoPrintUrl = false;

        for (String noPrintUrl : noPrintUrlArray) {

            if (requestUrl.indexOf(noPrintUrl) > -1) {

                isNoPrintUrl = true;

            }

        }

        return isNoPrintUrl;

    }
}