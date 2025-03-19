package com.gl.myparadise.common.server.web.config;

import com.gl.myparadise.common.server.web.handler.FeignLocaleResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Locale;

@Configuration
public class FeignConfiguration {
    public static final String TRACE_ID_HEADER = "X-FEIGN-TRACE-ID";
    public static final String LANG_HEADER = "X-FEIGN-LANG";
    public static final Locale DEFAULT_LANG = Locale.forLanguageTag("en-US");

    @Bean
    public LocaleResolver localeResolver() {
        return new FeignLocaleResolver();
    }

}
