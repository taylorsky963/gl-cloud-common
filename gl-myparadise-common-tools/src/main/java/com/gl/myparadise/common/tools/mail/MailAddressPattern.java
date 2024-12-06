package com.gl.myparadise.common.tools.mail;

import java.util.regex.Pattern;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: MailAddressPattern
 * @ProjectName: myparadise-common
 * @Date 2023-04-18 - 1:08
 */
public class MailAddressPattern {

    private MailAddressPattern() {
    }


    //多人邮件地址以;分割，;结尾
    private static final String REGEX_EMAIL_MULTI = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,};(([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,};)*$";

    /**
     * 正则表达式：验证单个邮箱（允许英文大小写字母、数字、下划线、英文句号、以及中划线组成）
     */
    private static final String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";


    public static boolean isEmail(String email) {
        // return Pattern.matches(REGEX_EMAIL, email);
        return Pattern.matches(REGEX_EMAIL_MULTI, email);
    }

    public static boolean isEmailPerson(String email) {
        // return Pattern.matches(REGEX_EMAIL, email);
        return Pattern.matches(REGEX_EMAIL, email);

    }

}
