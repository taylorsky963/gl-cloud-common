package com.gl.myparadise.common.mvc.cookies;

import com.gl.myparadise.common.mvc.constants.WebBaseVariableConstants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.Optional;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: LanguageCookieLocaleResolver
 * @ProjectName: myparadise-common
 * @Date 2023-05-01 - 22:48
 */
public class LanguageCookieLocaleResolver extends AcceptHeaderLocaleResolver {

    private static final Logger logger = LoggerFactory.getLogger(LanguageCookieLocaleResolver.class);

    public LanguageCookieLocaleResolver(){

        logger.info("set LanguageCookieLocaleResolver is start");

//        this.setCookieName(WebBaseVariableConstants.WEB_LANGUAGE_COOKIES_NAME);
//        //cookie有效期300天
//        this.setCookieMaxAge(WebBaseVariableConstants.WEB_LANGUAGE_COOKIES_VALID_PERIOD);

        this.setDefaultLocale(Locale.US);

        logger.info("set LanguageCookieLocaleResolver is OK");

    }

    @Override
    public Locale resolveLocale(HttpServletRequest request) {

        // **先从 Header 取**
        String language = request.getHeader(WebBaseVariableConstants.WEB_LANGUAGE_HEADER_NAME);

        if (StringUtils.isNotEmpty(language)) {

            Locale locale = Locale.forLanguageTag(language.split(",")[0]);
            // 解析第一个语言项
            logger.debug("Using locale from Accept-Language header: {}", locale);

            return locale;
        }

        // **2️如果 Header 里没有，从 Cookie 获取**
        String cookieLang = Optional.ofNullable(WebUtils.getCookie(request, WebBaseVariableConstants.WEB_LANGUAGE_COOKIES_NAME))
                .map(cookie -> cookie.getValue())
                .orElse(null);
        if (cookieLang != null && !cookieLang.isEmpty()) {

            Locale locale = Locale.forLanguageTag(cookieLang);

            logger.info("Using locale from cookie: {}", locale);

            return locale;

        }

        // **如果都没有，返回默认语言**
        logger.debug("Using default locale: {}", getDefaultLocale());

        return getDefaultLocale();
    }
}
