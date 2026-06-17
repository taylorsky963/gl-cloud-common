package com.gl.myparadise.common.tools.phone;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberToCarrierMapper;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import com.google.i18n.phonenumbers.geocoding.PhoneNumberOfflineGeocoder;

import java.util.Locale;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: PhoneUtil
 * @ProjectName: myparadise-common
 * @Date 2023-04-18 - 0:19
 */
public class PhoneUtil {

    private static PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

    private static PhoneNumberToCarrierMapper carrierMapper = PhoneNumberToCarrierMapper.getInstance();

    private static PhoneNumberOfflineGeocoder geocoder = PhoneNumberOfflineGeocoder.getInstance();

    private static PhoneUtil m_instance = null;

    private PhoneUtil() {
    }

    /**
     *
     * @param phoneNumber eg 139XXXXXXXX
     * @param countryCode eg 86
     * @return
     */
    public static boolean checkPhoneNumber(String phoneNumber, Integer countryCode) {

        long phone = Long.parseLong(phoneNumber);

        PhoneNumber pn = new PhoneNumber();

        pn.setCountryCode(countryCode);

        pn.setNationalNumber(phone);

        return phoneNumberUtil.isValidNumber(pn);

    }

    public static boolean checkPhoneNumberBringCountryCode(String phoneNumber) {

        try {

            PhoneNumber cn = phoneNumberUtil.parse(phoneNumber, "CN");

            return phoneNumberUtil.isValidNumber(cn);

        } catch (NumberParseException e) {

            e.printStackTrace();

        }

        return Boolean.FALSE;

    }


    /**
     * 根据国家代码和手机号  判断手机运营商
     * @param phoneNumber
     * @param countryCode
     * @return
     */
    public static String getCarrier(String phoneNumber, Integer countryCode){

        long phone = Long.parseLong(phoneNumber);

        PhoneNumber pn = new PhoneNumber();

        pn.setCountryCode(countryCode);

        pn.setNationalNumber(phone);

        //返回结果只有英文，自己转成成中文
        String carrierEn = carrierMapper.getNameForNumber(pn, Locale.ENGLISH);

        String carrierZh = "";

        carrierZh += geocoder.getDescriptionForNumber(pn, Locale.CHINESE);

        switch (carrierEn) {

            case "China Mobile":
                carrierZh += "移动";
                break;

            case "China Unicom":
                carrierZh += "联通";
                break;

            case "China Telecom":
                carrierZh += "电信";
                break;

            default:
                break;

        }

        return carrierZh;

    }

    /**
     * 根据国家代码和手机号  手机归属地
     * @param phoneNumber
     * @param countryCode
     * @return
     */
    public static String getGeo(String phoneNumber, Integer countryCode){

        long phone = Long.parseLong(phoneNumber);

        PhoneNumber pn = new PhoneNumber();
        pn.setCountryCode(countryCode);
        pn.setNationalNumber(phone);

        return geocoder.getDescriptionForNumber(pn, Locale.CHINESE);
    }

    /**
     * 菲律宾手机号格式校验（与登录/注册页规则一致）
     * 规则：09 开头 + 11 位，或 9 开头 + 10 位
     * @param phone 待校验手机号
     * @return true=格式合法
     */
    public static boolean isValidPhPhone(String phone) {
        if (phone == null || phone.isEmpty()) {
            return false;
        }
        return phone.matches("^09\\d{9}$") || phone.matches("^9\\d{9}$");
    }

    public static void main(String args[]) throws NumberParseException {

        System.out.println(checkPhoneNumber("7788870630",86));

    }

}
