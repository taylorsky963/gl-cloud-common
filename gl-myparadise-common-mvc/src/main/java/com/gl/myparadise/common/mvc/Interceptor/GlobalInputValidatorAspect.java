package com.gl.myparadise.common.mvc.Interceptor;

import com.gl.myparadise.common.mvc.utils.XSSStringValidatorUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Vincent
 * @description:
 * @className: GlobalInputValidatorAspect
 * @projectName: gl-cloud-base
 * @date 2025-07-23 - 00:19
 */
@Aspect
@Component
@Slf4j
public class GlobalInputValidatorAspect {

    @Pointcut("execution(* com.gl.myparadise.website.*.web.controller..*(..)) && " +
            "(@annotation(org.springframework.web.bind.annotation.PostMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.GetMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.RequestMapping))")
    public void controllerMethods() {}

    @Before("controllerMethods()")
    public void validateStringParams(JoinPoint joinPoint) {

        for (Object arg : joinPoint.getArgs()) {

            if (arg instanceof String) {

                XSSStringValidatorUtils.validateRawString((String) arg, "StringArg");

            } else if (arg instanceof Map) {

                ((Map<?, ?>) arg).forEach((k, v) -> {

                    if (v instanceof String) {

                        XSSStringValidatorUtils.validateRawString((String) v, k.toString());

                    }
                });

            } else {

                XSSStringValidatorUtils.validateStringFields(arg);

            }
        }
    }
}
