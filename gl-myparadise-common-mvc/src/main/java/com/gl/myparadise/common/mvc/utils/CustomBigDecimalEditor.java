package com.gl.myparadise.common.mvc.utils;

import org.springframework.util.StringUtils;

import java.beans.PropertyEditorSupport;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: CustomBigDecimalEditor
 * @ProjectName: myparadise-common
 * @Date 2023-05-01 - 22:37
 */
public class CustomBigDecimalEditor extends PropertyEditorSupport {

    public void setAsText(String text) throws IllegalArgumentException {
        if (StringUtils.isEmpty(text)) {
            // Treat empty String as null value.
            setValue(null);
        } else {
            setValue(NumberUtils.getBigDecimal(text));
        }
    }
}
