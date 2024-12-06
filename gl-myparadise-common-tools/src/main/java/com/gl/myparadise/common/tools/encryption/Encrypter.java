package com.gl.myparadise.common.tools.encryption;

import com.gl.myparadise.common.tools.util.MyBase64Support;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: Encrypter
 * @ProjectName: myparadise-common
 * @Date 2023-04-18 - 0:17
 */
public class Encrypter {

    private static Cipher ecipher;

    private static Cipher dcipher;

    // 必须24个字符
    private static final String key = "*:@7$8!t*:@7$8!t*:@7$8!t";

    private static final String alg = "DESede";

    static {

        try {

            SecretKey skey = new SecretKeySpec(key.getBytes(), alg);

            ecipher = Cipher.getInstance(alg);

            dcipher = Cipher.getInstance(alg);

            ecipher.init(Cipher.ENCRYPT_MODE, skey);

            dcipher.init(Cipher.DECRYPT_MODE, skey);

        } catch (Exception e) {

            e.printStackTrace();

        }
    }

    /**
     *
     * <pre>
     * 加密
     * </pre>
     *
     * @param str
     * @return
     */
    public static String encrypt(String str) {
        if (str == null)
            return "";
        try {
            // Encode the string into bytes using utf-8
            byte[] utf8 = str.getBytes("UTF8");

            // Encrypt
            byte[] enc = ecipher.doFinal(utf8);

            // Encode bytes to base64 to get a string
            return MyBase64Support.toStr(enc);

        } catch (Exception e) {

            e.printStackTrace();

            return "";
        }

    }

    /**
     *
     * <pre>
     * 解密
     * </pre>
     *
     * @param str
     * @return
     */
    public static String decrypt(String str) {
        if (str == null)
            return "";
        try {
            // Decode base64 to get bytes
            byte[] dec = MyBase64Support.fromStr(str);

            // Decrypt
            byte[] utf8 = dcipher.doFinal(dec);

            // Decode using utf-8
            return new String(utf8, "UTF8");

        } catch (Exception e) {

            e.printStackTrace();

            return "";
        }

    }
    public  static void main(String[] args){
        System.out.println(decrypt("mXrDVGMo6XTu7T1K_QQCSQ.."));
        System.out.println(encrypt("botdb"));
        System.out.println(encrypt("bot@1qaz2wsx"));
        System.out.println(encrypt("wfybsq_mysql_Pw#%&(sky"));
    }
}
