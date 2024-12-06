package com.gl.myparadise.common.mvc.language;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gl.myparadise.common.tools.jason.GJonUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.servlet.ServletRequest;
import java.io.IOException;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

@Component
public class I18nConfigHandler {

    private static final Logger logger = LoggerFactory.getLogger(I18nConfigHandler.class);

    @Autowired
    private static MessageSource messageSource;

    public I18nConfigHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    protected static String get(String msgKey, Object... args) {

        try {

            return messageSource.getMessage(msgKey, args, msgKey, LocaleContextHolder.getLocale());

        } catch (Exception e) {

            logger.error("I18nUtils.get is error ", e);

        }

        return msgKey;
    }

    protected static String get(ServletRequest request, String msgKey, Object... args) {

        try {

            String locale = request.getParameter("lang");

            if (StringUtils.isEmpty(locale) ||"undefined".equals(locale)) {

                locale = "zh_CN";

            }

            return messageSource.getMessage(msgKey, args, msgKey, org.springframework.util.StringUtils.parseLocaleString(locale));

        } catch (Exception e) {

            logger.error("I18nUtils.get is error ", e);

        }

        return msgKey;
    }

    protected static String get(Locale locale, String msgKey, Object... args) {

        try {

            return messageSource.getMessage(msgKey, args, msgKey, locale);

        } catch (Exception e) {

            logger.error("I18nUtils.get is error ", e);

        }

        return msgKey;

    }

    protected static I18nMsgModel builder(String tpl) {
        return new I18nMsgModel(tpl);
    }

    protected static String currentI18n(String json){
        if(StringUtils.isEmpty(json)){
            return json;
        }
        Map<String, String> map = isJSONValid(json)? GJonUtil.toMap(json):null;
        if(CollectionUtils.isEmpty(map)){
            return json;
        }
        String id = map.get("id");
        id = get(id);
        Object data = map.get("data");
        Map<String, String> dataMap = GJonUtil.parse(data, Map.class);
        Iterator<String> iterator = dataMap.keySet().iterator();
        while (iterator.hasNext()){
            String key = iterator.next();
            String paramKey = "{" + key + "}";
            String value = String.valueOf(dataMap.get(key));
            if(value.startsWith("{")&&value.endsWith("}")){
                value = currentI18n(value);
            }
            id = StringUtils.replace(id, paramKey, value);
        }
        return id;
    }

    protected static boolean isJSONValid(String jsonInString ) {

        if(!jsonInString.startsWith("{")&&!jsonInString.endsWith("}")){

            return false;

        }
        try {

            final ObjectMapper mapper = new ObjectMapper();

            mapper.readTree(jsonInString);

            return true;

        } catch (IOException e) {

            logger.error("I18nUtils.isJSONValid is error ", e);

        }

        return false;

    }
}
