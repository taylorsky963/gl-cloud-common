package com.gl.myparadise.common.mvc.Interceptor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gl.myparadise.common.bean.model.ServiceRsModel;
import com.gl.myparadise.common.bean.report.system.SystemOperationLogReport;
import com.gl.myparadise.common.consumer.rabbitmq.producer.service.ReportService;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@Aspect
@Component
@Slf4j
public class SystemOperationLogAspect {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ReportService reportService;

    @Pointcut(value = "execution(* com.gl.myparadise.background..controller..*(..))")
    public void backendPointCut() {
    }

    @Pointcut(value = "@annotation(com.gl.myparadise.common.mvc.annotation.IgnoreOperationLog)")
    public void ignorePointCut() {
    }

    @Pointcut("backendPointCut() && !ignorePointCut()")
    public void pointCut() {
    }

    @Around("pointCut()")
    public Object after(ProceedingJoinPoint pjp) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String requestURL = request.getRequestURI();
        String menuId = request.getHeader("menuId");
        if (StringUtils.isBlank(menuId)) {
            return pjp.proceed();
        }

        String uid = request.getHeader("uid");
        if (StringUtils.isBlank(uid)) {
            return pjp.proceed();
        }

        Object result = pjp.proceed();

        try {
            String[] parameterNamesArgs = ((MethodSignature) pjp.getSignature()).getParameterNames();
            Object[] args = pjp.getArgs();
            SystemOperationLogReport build = SystemOperationLogReport.builder()
                    .adminId(Integer.parseInt(uid))
                    .menuId(menuId)
                    .operationTime(System.currentTimeMillis())
                    .url(requestURL)
                    .param(this.getParamStr(parameterNamesArgs, args))
                    .result(this.getResultStr(result))
                    .build();

            reportService.systemOperationReport(build);
        } catch (Exception e) {
            log.debug("操作日志记录错误, {}", requestURL, e);
        }


        return pjp.proceed();
    }

    private String getParamStr(String[] parameterNamesArgs, Object[] args) {
        HashMap<Object, Object> param = Maps.newHashMap();
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof ServletRequest || args[i] instanceof ServletResponse || args[i] instanceof MultipartFile) {
                continue;
            }
            if (args[i] instanceof BindingResult) {
                continue;
            }
            param.put(parameterNamesArgs[i], args[i]);
        }
        try {
            return objectMapper.writeValueAsString(param);
        } catch (Exception e) {
            return getParamStr2(parameterNamesArgs, args);
        }
    }

    private String getParamStr2(String[] parameterNamesArgs, Object[] args) {
        StringBuilder paramsBuf = new StringBuilder();
        // 获取请求参数集合并进行遍历拼接
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof ServletRequest || args[i] instanceof ServletResponse || args[i] instanceof MultipartFile) {
                continue;
            }
            if (args[i] instanceof BindingResult) {
                continue;
            }
            if (paramsBuf.length() > 0) {
                paramsBuf.append("|");
            }
            paramsBuf.append(parameterNamesArgs[i]).append(" = ").append(args[i]);
        }
        return paramsBuf.toString();
    }

    private String getResultStr(Object obj) throws JsonProcessingException {
        if (obj instanceof ServiceRsModel) {
            ServiceRsModel res = (ServiceRsModel) obj;
            HashMap<Object, Object> data = Maps.newHashMap();
            data.put("rsCode", res.getRsCode());
            data.put("rsMessages", res.getRsMessages());
            return objectMapper.writeValueAsString(data);
        } else {
            return null;
        }
    }
}
