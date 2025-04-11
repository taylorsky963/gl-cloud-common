package com.gl.myparadise.common.tools.bean;


public class FileUtils {

    /**
     * 根据文件名字判断文件类型
     */
    public static String getTypeByName(String fileName) {
        if (fileName.lastIndexOf(".") != -1) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        } else {
            return null;
        }
    }


}
