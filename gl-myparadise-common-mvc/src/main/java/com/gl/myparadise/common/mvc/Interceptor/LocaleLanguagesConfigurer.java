package com.gl.myparadise.common.mvc.Interceptor;

import com.gl.myparadise.common.mvc.constants.WebBaseVariableConstants;
import com.gl.myparadise.common.mvc.cookies.LanguageCookieLocaleResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: LocaleLanguagesConfigurer
 * @Date 2023-05-11 - 0:49
 */
@Configuration
public class LocaleLanguagesConfigurer implements WebMvcConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(LocaleLanguagesConfigurer.class);

    @Bean
    public LocaleResolver localeResolver() {

       return new LanguageCookieLocaleResolver();

    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        // 参数名，从请求的lang参数获取区域信息
        lci.setParamName(WebBaseVariableConstants.WEB_LANGUAGE_COOKIES_NAME);

        return lci;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(localeChangeInterceptor());
    }
}
