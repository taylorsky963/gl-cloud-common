package com.gl.myparadise.common.mvc.controller;

import com.gl.myparadise.common.mvc.utils.CustomBigDecimalEditor;
import com.gl.myparadise.common.mvc.utils.QueryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: BaseWebController
 * @ProjectName: myparadise-common
 * @Date 2023-05-01 - 22:34
 */
public abstract class BaseWebController {

    private static final Logger logger = LoggerFactory.getLogger(BaseWebController.class);

    public static int pageSize = 10;

    public Map getParameters(HttpServletRequest request) {

        return request.getParameterMap();

    }


    public void responseMsg(HttpServletResponse response, String content) throws Exception {
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(content);
    }

    public String getQueryStringWithoutPageNum(HttpServletRequest request) {
        Map<String, Object> m = getParameters(request);
        m.remove("pageNum");
        return QueryUtil.getQueryString(m);
    }

    public String getQueryStringWithoutPageInfo(HttpServletRequest request) {
        Map<String, Object> m = getParameters(request);
        m.remove("pageNum");
        m.remove("pageSize");
        return QueryUtil.getQueryString(m);
    }

    public Map<String, Object> getQueryMapWithoutPageInfo(HttpServletRequest request) {
        Map<String, Object> m = getParameters(request);
        m.remove("pageNum");
        m.remove("pageSize");
        return m;
    }

    public Map<String, String> getQueryMap(HttpServletRequest request) {
        Map<String, Object> map = getQueryMapWithoutPageInfo(request);
        Map<String, String> map0 = new HashMap<String, String>();
        Iterator<String> iter = map.keySet().iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            String val = QueryUtil.getParamValue(map, key);
            map0.put(key, val);
        }
        return map0;

    }

    public void writeJson(String json,HttpServletResponse servletResponse) {

        PrintWriter pw = null;

        try {

            servletResponse.setContentType("text/plain;charset=UTF-8");

            pw = servletResponse.getWriter();

            pw.write(json);

            pw.flush();

            pw.close();

        } catch (IOException e) {

            logger.error("BaseWebFormController writeJson", e);

        } finally {

            if (pw != null) {

                pw.close();

            }
        }
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);

        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));

        CustomBigDecimalEditor bigDecimalEditor = new CustomBigDecimalEditor();

        binder.registerCustomEditor(BigDecimal.class, bigDecimalEditor);


    }
}
