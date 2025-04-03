package com.gl.myparadise.common.webclient.util;

import com.gl.myparadise.common.bean.model.ServiceRsModel;
import com.gl.myparadise.common.tools.jason.GJonUtil;
import com.gl.myparadise.common.tools.jason.GJonUtilNamingPolicy;
import com.gl.myparadise.common.webclient.constants.WebClientConstants;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: HttpResponseErrorUtils
 * @ProjectName: gi-matrix-parent
 * @Date 2023-06-26 - 3:52 p.m.
 */
public class HttpResponseStatusUtils {

    private HttpResponseStatusUtils(){}

    public static ServiceRsModel<Object> buildHttpResponseBean(String rsJason, Class rsDataClass){

        ServiceRsModel<Object> rsModel = new ServiceRsModel<Object>();

        if(rsJason != null){

            HttpResponseData matrixResponseData = GJonUtil.parse(rsJason, HttpResponseData.class);

            if (StringUtils.isNotEmpty(matrixResponseData.getResponseStatus())
                    && StringUtils.isNotEmpty(matrixResponseData.getResponseErrorCode())
                    && matrixResponseData.getResponseStatus().equals(WebClientConstants.RESPONSE_ERROR_TGA)) {

                rsModel.error(matrixResponseData.getResponseErrorCode(),matrixResponseData.getResponseErrorMessages());

            } else {

                Object rsData = GJonUtilNamingPolicy.parse(rsJason, rsDataClass);

                rsModel.setRsData(rsData);

            }
        }


        return rsModel;

    }

    public static ServiceRsModel<?> buildHttpResponseBeanForAllType(String rsJason, Class rsDataClass){

        ServiceRsModel<Object> rsModel = new ServiceRsModel<Object>();

        if(rsJason != null){

            Object rsData = GJonUtilNamingPolicy.parse(rsJason, rsDataClass);

            rsModel.setRsData(rsData);

        }

        return rsModel;

    }

}
