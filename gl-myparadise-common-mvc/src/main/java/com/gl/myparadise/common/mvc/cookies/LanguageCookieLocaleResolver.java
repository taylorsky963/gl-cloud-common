package com.gl.myparadise.common.mvc.cookies;

import com.gl.myparadise.common.mvc.constants.WebBaseVariableConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import java.util.Locale;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: LanguageCookieLocaleResolver
 * @ProjectName: myparadise-common
 * @Date 2023-05-01 - 22:48
 */
public class LanguageCookieLocaleResolver extends CookieLocaleResolver {

    private static final Logger logger = LoggerFactory.getLogger(LanguageCookieLocaleResolver.class);

    public LanguageCookieLocaleResolver(){

        logger.info("set LanguageCookieLocaleResolver is start");

        this.setCookieName(WebBaseVariableConstants.WEB_LANGUAGE_COOKIES_NAME);
        //cookie有效期300天
        this.setCookieMaxAge(WebBaseVariableConstants.WEB_LANGUAGE_COOKIES_VALID_PERIOD);

        this.setDefaultLocale(Locale.US);

        logger.info("set LanguageCookieLocaleResolver is OK");

    }
}
