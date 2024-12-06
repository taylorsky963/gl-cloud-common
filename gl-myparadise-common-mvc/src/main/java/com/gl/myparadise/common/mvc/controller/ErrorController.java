package com.gl.myparadise.common.mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: ErrorController
 * @ProjectName: myparadise-common
 * @Date 2023-05-01 - 22:38
 */
@Controller
@RequestMapping("/error")
public class ErrorController {

    @RequestMapping(value = "/404")
    public String error_404() {

        return "/error/404";

    }

    @RequestMapping(value = "/500")
    public String error_500() {

        return "/error/500";

    }
}
