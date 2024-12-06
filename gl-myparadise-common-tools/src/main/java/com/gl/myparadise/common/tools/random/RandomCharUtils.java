package com.gl.myparadise.common.tools.random;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: RandomCharUtils
 * @ProjectName: myparadise-common
 * @Date 2023-04-18 - 1:10
 */
public class RandomCharUtils {

    private RandomCharUtils(){}

    private static char[] hexChar = new char[] { '0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

    public static byte[] toHex(byte[] digestByte) {
        byte[] rtChar = new byte[digestByte.length * 2];
        for (int i = 0; i < digestByte.length; i++) {
            byte b1 = (byte) (digestByte[i] >> 4 & 0x0f);
            byte b2 = (byte) (digestByte[i] & 0x0f);
            rtChar[i * 2] = (byte) (b1 < 10 ? b1 + 48 : b1 + 55);
            rtChar[i * 2 + 1] = (byte) (b2 < 10 ? b2 + 48 : b2 + 55);
        }
        return rtChar;
    }

    public static String toHexString(byte[] digestByte) {
        return new String(toHex(digestByte));
    }

    public static byte[] fromHex(byte[] sc) {
        byte[] res = new byte[sc.length / 2];
        for (int i = 0; i < sc.length; i++) {
            byte c1 = (byte) (sc[i] - 48 < 17 ? sc[i] - 48 : sc[i] - 55);
            i++;
            byte c2 = (byte) (sc[i] - 48 < 17 ? sc[i] - 48 : sc[i] - 55);
            res[i / 2] = (byte) (c1 * 16 + c2);
        }
        return res;
    }

    public static byte[] fromHexString(String hex) {
        return fromHex(hex.getBytes());
    }

    public static String encode(String in) {
        return new String(toHex(in.getBytes()));
    }

    public static byte[] strToByte(String inStr) {
        if (!StringUtils.isNumeric(inStr)) {
            return null;
        }
        ArrayList byteList = new ArrayList();
        for (int i = 0; i < inStr.length(); i++) {
            byteList.add(new Byte(
                    Byte.parseByte(String.valueOf(inStr.charAt(i)))));
        }
        byte[] byteArray = new byte[byteList.size()];
        for (int j = 0; j < byteArray.length; j++) {
            byteArray[j] = ((Byte) byteList.get(j)).byteValue();

        }
        return byteArray;
    }

    public static String decode(String in) {
        return new String(fromHex(in.getBytes()));
    }

    public static String getEncryptKey() {
        return getEncryptKey(24);
    }

    public static String getEncryptKey(int num) {
        return randomString(num);
    }

    public static final String randomString(int length) {
        char[] numbersAndLetters = null;
        if (length < 1) {
            return null;
        }
        numbersAndLetters = ("0123456789abcdefghijkmnpqrstuvwxyzABCDEFGHIJKLMNPQRSTUVWXYZ@#$%&*")
                .toCharArray();
        char[] randBuffer = new char[length];
        for (int i = 0; i < randBuffer.length; i++) {
            randBuffer[i] = numbersAndLetters[ThreadLocalRandom.current().nextInt(0,65)];
        }
        return new String(randBuffer);
    }

    public static final String randomNumAndString(int length) {
        char[] numbersAndLetters = null;
        if (length < 1) {
            return null;
        }
        numbersAndLetters = ("0123456789abcdefghijkmnpqrstuvwxyzABCDEFGHIJKLMNPQRSTUVWXYZ")
                .toCharArray();
        char[] randBuffer = new char[length];
        for (int i = 0; i < randBuffer.length; i++) {
            randBuffer[i] = numbersAndLetters[ThreadLocalRandom.current().nextInt(0,59)];
        }
        return new String(randBuffer);
    }

    public static int getHashCode() {
        return randomString(10).hashCode();
    }

    public static void main(String args[]) {

        long startTime = System.currentTimeMillis();

        System.out.println(getEncryptKey(12));

    }
}
