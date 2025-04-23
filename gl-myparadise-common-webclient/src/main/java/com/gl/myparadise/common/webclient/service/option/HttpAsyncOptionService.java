package com.gl.myparadise.common.webclient.service.option;

import com.gl.myparadise.common.bean.model.ServiceRsModel;
import com.gl.myparadise.common.webclient.model.request.stream.StreamDownLoadRequestModel;
import com.gl.myparadise.common.webclient.model.request.stream.StreamUploadRequestModel;

public interface HttpAsyncOptionService {


    ServiceRsModel<Object> sendRequestPostHttpServer(String webClientBeanName, String threadPoolBeanName, String uri, Object data, Class rsDataClass);
    ServiceRsModel<Object> sendRequestPostFormHttpServer(String webClientBeanName, String threadPoolBeanName, String uri, Object data, Class rsDataClass);

    ServiceRsModel<Object> sendRequestPutHttpServer(String webClientBeanName, String threadPoolBeanName, String uri, Object data, Class rsDataClass);

    ServiceRsModel<Object> sendRequestPutHttpServerHeader(String webClientBeanName, String threadPoolBeanName, String uri, Object data, Object header, Class rsDataClass);

    ServiceRsModel<Object> sendRequestGetHttpServerAllParams(String webClientBeanName, String threadPoolBeanName, String uri, Object data, Class rsDataClass);

    ServiceRsModel<Object> sendRequestGetHttpServer(String webClientBeanName, String threadPoolBeanName, String uri, Object data, Class rsDataClass);

    ServiceRsModel<?> sendRequestGetHttpServerForAllTypeAllParams(String webClientBeanName, String threadPoolBeanName, String uri, Object data, Class rsDataClass);

    ServiceRsModel<?> sendRequestGetHttpServerForAllType(String webClientBeanName, String threadPoolBeanName, String uri, Object data, Class rsDataClass);

    ServiceRsModel<Object> sendRequestGetMultiValueMapHttpServerAllParams(String webClientBeanName, String threadPoolBeanName, String uri, Object data, Class rsDataClass);

    ServiceRsModel<Object> sendRequestGetMultiValueMapHttpServer(String webClientBeanName, String threadPoolBeanName, String uri, Object data, Class rsDataClass);

    ServiceRsModel<Object> sendRequestStreamUploadHttpServer(String webClientBeanName, String threadPoolBeanName, String uri
            , StreamUploadRequestModel streamUploadRequestModel, Class rsDataClass);

    ServiceRsModel<Object> sendRequestStreamDownLoadHttpServer(String webClientBeanName, String threadPoolBeanName, String uri
            , StreamDownLoadRequestModel streamDownLoadRequestModel, Class rsDataClass);

    ServiceRsModel<Object> sendRequestDeleteHttpServer(String webClientBeanName, String threadPoolBeanName, String uri, Object data, Class rsDataClass);
}
