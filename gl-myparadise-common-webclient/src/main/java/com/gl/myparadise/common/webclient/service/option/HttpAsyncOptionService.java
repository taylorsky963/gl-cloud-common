package com.gl.myparadise.common.webclient.service.option;

import com.gl.myparadise.common.bean.model.ServiceRsModel;
import com.gl.myparadise.common.webclient.model.request.stream.StreamDownLoadRequestModel;
import com.gl.myparadise.common.webclient.model.request.stream.StreamUploadRequestModel;

public interface HttpAsyncOptionService {


    ServiceRsModel<Object> sendRequestPostMatrixServer(String webClientBeanName, String threadPoolBeanName, String uri, Object data, Class rsDataClass);

    ServiceRsModel<Object> sendRequestPutMatrixServer(String webClientBeanName, String threadPoolBeanName, String uri, Object data, Class rsDataClass);

    ServiceRsModel<Object> sendRequestPutMatrixServerHeader(String webClientBeanName, String threadPoolBeanName, String uri, Object data, Object header, Class rsDataClass);

    ServiceRsModel<Object> sendRequestGetMatrixServer(String webClientBeanName, String threadPoolBeanName, String uri, Object data, Class rsDataClass);

    ServiceRsModel<?> sendRequestGetMatrixServerForAllType(String webClientBeanName, String threadPoolBeanName, String uri, Object data, Class rsDataClass);

    ServiceRsModel<Object> sendRequestGetMultiValueMapMatrixServer(String webClientBeanName, String threadPoolBeanName, String uri, Object data, Class rsDataClass);

    ServiceRsModel<Object> sendRequestStreamUploadMatrixServer(String webClientBeanName, String threadPoolBeanName, String uri
            , StreamUploadRequestModel streamUploadRequestModel, Class rsDataClass);

    ServiceRsModel<Object> sendRequestStreamDownLoadMatrixServer(String webClientBeanName, String threadPoolBeanName, String uri
            , StreamDownLoadRequestModel streamDownLoadRequestModel, Class rsDataClass);

    ServiceRsModel<Object> sendRequestDeleteMatrixServer(String webClientBeanName, String threadPoolBeanName, String uri, Object data, Class rsDataClass);
}
