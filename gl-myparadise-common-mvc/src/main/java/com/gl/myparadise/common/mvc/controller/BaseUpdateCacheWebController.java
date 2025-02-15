package com.gl.myparadise.common.mvc.controller;

import com.gl.myparadise.common.bean.model.ServiceRsModel;
import com.gl.myparadise.common.bean.model.WebCacheMqModel;
import com.gl.myparadise.common.tools.jason.GJonUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: BaseWebController
 * @ProjectName: myparadise-common
 * @Date 2023-05-01 - 22:34
 */
@Slf4j
public abstract class BaseUpdateCacheWebController extends BaseWebController {

    public ServiceRsModel<Integer> updateClearRedisKeyEncapsulateResults(ServiceRsModel<Integer> serviceRsModel, WebCacheMqModel webCacheMqModel){

        if(serviceRsModel.isSuccess() && webCacheMqModel != null){

            clearRedisKey(webCacheMqModel);

        }

        return serviceRsModel;

    }

    public void buildCacheMqModel(WebCacheMqModel webCacheMqModel, String sendType, String sendBussesCode, String opTypeCode, Object object, boolean isBean){

        webCacheMqModel.setSendType(sendType);

        webCacheMqModel.setSendBussesCode(sendBussesCode);

        webCacheMqModel.setOpTypeCode(opTypeCode);

        if(isBean){

            webCacheMqModel.setData(GJonUtil.toJson(object));

        }else{

            webCacheMqModel.setData(object);

        }

    }

    public abstract void clearRedisKey(Object object);
}
