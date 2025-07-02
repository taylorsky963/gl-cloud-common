package com.gl.myparadise.common.webclient.util;

import org.springframework.http.MediaType;

import javax.activation.MimetypesFileTypeMap;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: HttpMediaDataUtils
 * @ProjectName: gl-matrix-parent
 * @Date 2023-09-05 - 6:51 p.m.
 */
public class HttpMediaDataUtils {

    private HttpMediaDataUtils(){}

    public static MediaType getMediaType(String medisName){

        return MediaType.valueOf(new MimetypesFileTypeMap().getContentType(medisName));

    }

    public static String getContentType(String medisName){

        return new MimetypesFileTypeMap().getContentType(medisName);

    }


}