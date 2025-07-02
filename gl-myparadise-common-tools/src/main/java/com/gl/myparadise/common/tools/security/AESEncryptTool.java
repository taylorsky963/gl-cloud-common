package com.gl.myparadise.common.tools.security;


import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

@Slf4j
public class AESEncryptTool {

    private static final String AES_MODE = "AES/CBC/PKCS5Padding";

    /**
     * AES 加密，返回 Base64(IV + 加密内容)
     */
    public static String encrypt(String plainText, String key) {

        try {

            byte[] keyBytes = key.getBytes("UTF-8");

            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");

            // 随机生成 16 字节 IV
            byte[] ivBytes = new byte[16];

            new SecureRandom().nextBytes(ivBytes);

            IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);

            Cipher cipher = Cipher.getInstance(AES_MODE);

            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

            byte[] encrypted = cipher.doFinal(plainText.getBytes("UTF-8"));

            // 拼接 IV + 加密内容
            byte[] finalBytes = new byte[ivBytes.length + encrypted.length];

            System.arraycopy(ivBytes, 0, finalBytes, 0, ivBytes.length);

            System.arraycopy(encrypted, 0, finalBytes, ivBytes.length, encrypted.length);

            return Base64.getEncoder().encodeToString(finalBytes);

        } catch (Exception e) {

            log.error("AES加密出错: {}", e.getMessage(), e);

            return null;
        }

    }

    /**
     * 解密 Base64(IV + 加密内容)
     */
    public static String decrypt(String base64CipherText, String key) {

        try {

            byte[] allBytes = Base64.getDecoder().decode(base64CipherText);

            byte[] ivBytes = Arrays.copyOfRange(allBytes, 0, 16);

            byte[] cipherBytes = Arrays.copyOfRange(allBytes, 16, allBytes.length);

            IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);

            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance(AES_MODE);

            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

            byte[] decrypted = cipher.doFinal(cipherBytes);

            return new String(decrypted, "UTF-8");

        } catch (Exception e) {

            log.error("AES解密出错: {}", e.getMessage(), e);

            return null;

        }
    }

    public static void main(String[] args) {
        String key = "1234567890abcdef"; // 必须是 16 字节
        String content = "prin@0630";

        String encrypted = encrypt(content, key);
        System.out.println("加密结果（Base64）: " + encrypted);

        String decrypted = decrypt(encrypted, key);
        System.out.println("解密结果: " + decrypted);
    }

}
