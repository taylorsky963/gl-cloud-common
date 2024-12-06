package com.gl.myparadise.common.tools.util;

import java.io.IOException;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: MyBase64Support
 * @ProjectName: myparadise-common
 * @Date 2023-04-18 - 1:15
 */
public class MyBase64Support {


    public static String toStr(byte[] bytes) {

        String str = new sun.misc.BASE64Encoder().encode(bytes);

        if (str == null)
            return "";

        str = str.replaceAll("\\+", "_");
        str = str.replaceAll("/", "-");
        str = str.replaceAll("=", ".");
        str = str.replaceAll("\\s", "");

        return str;
    }

    public static byte[] fromStr(String str) throws IOException {

        if (str == null)
            return null;

        str = str.replaceAll("_", "+");
        str = str.replaceAll("-", "/");
        str = str.replaceAll("\\.", "=");

        byte[] dec = new sun.misc.BASE64Decoder().decodeBuffer(str);

        return dec;
    }

}
