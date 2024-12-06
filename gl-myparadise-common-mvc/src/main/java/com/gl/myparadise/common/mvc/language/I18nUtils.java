package com.gl.myparadise.common.mvc.language;

import javax.servlet.ServletRequest;
import java.util.Locale;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: I18nUtils
 * @ProjectName: myparadise-common
 * @Date 2023-05-09 - 14:21
 */
public class I18nUtils {

    private I18nUtils(){};


    public static String get(String msgKey, Object... args) {

        return I18nConfigHandler.get(msgKey, args);

    }

    public static String get(ServletRequest request, String msgKey, Object... args) {

        return I18nConfigHandler.get(request, msgKey, args);

    }


    public static String get(Locale locale, String msgKey, Object... args) {

        return I18nConfigHandler.get(locale, msgKey, args);

    }
}
