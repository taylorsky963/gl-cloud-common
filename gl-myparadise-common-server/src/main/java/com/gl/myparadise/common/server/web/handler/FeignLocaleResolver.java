package com.gl.myparadise.common.server.web.handler;

import com.gl.myparadise.common.server.web.config.FeignConfiguration;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@Component
public class FeignLocaleResolver extends AcceptHeaderLocaleResolver {

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        String lang = request.getHeader(FeignConfiguration.LANG_HEADER);
        if (StringUtils.isEmpty(lang)) {
            return FeignConfiguration.DEFAULT_LANG;
        } else {
            return Locale.forLanguageTag(lang);
        }
    }
}