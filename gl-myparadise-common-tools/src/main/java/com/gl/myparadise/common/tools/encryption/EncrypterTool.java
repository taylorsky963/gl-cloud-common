package com.gl.myparadise.common.tools.encryption;

import java.io.IOException;
import java.util.Scanner;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: EncrypterTool
 * @ProjectName: myparadise-common
 * @Date 2023-04-18 - 0:18
 */
public class EncrypterTool {

    public static void main(String[] args) throws IOException {

        while (true) {

            encrypter();
        }

    }

    public static void encrypter() {

        System.out.print("请输入要加密的字符串:");

        Scanner sc = new Scanner(System.in);

        String encrypterString = sc.nextLine();

        if (encrypterString != null && !"".equals(encrypterString.trim())) {

            if ("exit".equals(encrypterString.trim().toLowerCase())) {

                System.exit(0);

            } else {

                System.out.println("加密的字串:" + Encrypter.encrypt(encrypterString.trim()));

            }

        } else {

            System.out.println("输入的字串为空，请重新输入");
        }

    }
}
