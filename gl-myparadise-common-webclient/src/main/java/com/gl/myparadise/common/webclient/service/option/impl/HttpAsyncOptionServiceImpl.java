package com.gl.myparadise.common.webclient.service.option.impl;

import com.gl.myparadise.common.webclient.model.request.stream.StreamUploadRequestModel;
import com.gl.myparadise.common.bean.model.ServiceRsModel;
import com.gl.myparadise.common.tools.bean.BeanMapUtil;
import com.gl.myparadise.common.tools.jason.GJonUtilNamingPolicy;
import com.gl.myparadise.common.webclient.model.request.WebClientRequestModel;
import com.gl.myparadise.common.webclient.model.request.stream.StreamDownLoadRequestModel;
import com.gl.myparadise.common.webclient.model.request.stream.WebClientStreamRequestModel;
import com.gl.myparadise.common.webclient.model.response.stream.StreamDownLoadResponseData;
import com.gl.myparadise.common.webclient.service.option.HttpAsyncOptionService;
import com.gl.myparadise.common.webclient.service.option.HttpWebClientService;
import com.gl.myparadise.common.webclient.util.HttpResponseStatusUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: MatrixAsyncOptionServiceImpl
 * @ProjectName: gi-matrix-parent
 * @Date 2023-06-26 - 12:21 a.m.
 */
@Service
@Slf4j
public class HttpAsyncOptionServiceImpl implements HttpAsyncOptionService {
    
    @Autowired
    private HttpWebClientService httpWebClientService;

    /**
     * http reques post 有header方法封装
     * @param uri
     * @param data
     * @param rsDataClass
     * @return
     */
    @Override
    public ServiceRsModel<Object> sendRequestPostHttpServer(String webClientBeanName, String threadPoolBeanName, String uri, Object data, Map<String, String> hearderMap, Class rsDataClass){

        try{


            Future<String> rsFuture = httpWebClientService.sendRequestPostHttpServer(buildWebClientRequestModelToData(webClientBeanName, threadPoolBeanName, uri, data, hearderMap));

            String rsJason = rsFuture.get();

            return HttpResponseStatusUtils.buildHttpResponseBean(rsJason, rsDataClass);

        } catch (Exception e) {

            log.error("sendRequestPostHttpServer ia error",e);

        }

        return new ServiceRsModel().error();

    }

    /**
     * http reques post 方法封装
     * @param uri
     * @param data
     * @param rsDataClass
     * @return
     */
    @Override
    public ServiceRsModel<Object> sendRequestPostHttpServer(String webClientBeanName, String threadPoolBeanName, String uri, Object data, Class rsDataClass){

        try{


            Future<String> rsFuture = httpWebClientService.sendRequestPostHttpServer(buildWebClientRequestModelToData(webClientBeanName, threadPoolBeanName, uri, data, null));

            String rsJason = rsFuture.get();

            return HttpResponseStatusUtils.buildHttpResponseBean(rsJason, rsDataClass);

        } catch (Exception e) {

            log.error("sendRequestPostHttpServer ia error",e);

        }

        return new ServiceRsModel().error();

    }

    @Override
    public ServiceRsModel<Object> sendRequestPostFormHttpServer(String webClientBeanName, String threadPoolBeanName, String uri, Object data, Class rsDataClass) {
        try{


            Future<String> rsFuture = httpWebClientService.sendRequestPostFormHttpServer(buildWebClientRequestModelToDataMap(webClientBeanName, threadPoolBeanName, uri, data, null,true));

            String rsJason = rsFuture.get();

            return HttpResponseStatusUtils.buildHttpResponseBean(rsJason, rsDataClass);

        } catch (Exception e) {

            log.error("sendRequestPostHttpServer ia error",e);

        }

        return new ServiceRsModel().error();
    }

    /**
     * http reques put 方法封装
     * @param uri
     * @param data
     * @param rsDataClass
     * @return
     */
    @Override
    public ServiceRsModel<Object> sendRequestPutHttpServer(String webClientBeanName, String threadPoolBeanName, String uri, Object data, Class rsDataClass){

        try{


            Future<String> rsFuture = httpWebClientService.sendRequestPutHttpServer(buildWebClientRequestModelToData(webClientBeanName, threadPoolBeanName, uri, data, null));

            String rsJason = rsFuture.get();

            return HttpResponseStatusUtils.buildHttpResponseBean(rsJason, rsDataClass);

        } catch (Exception e) {

            log.error("sendRequestPutHttpServer ia error",e);

        }

        return new ServiceRsModel().error();

    }

    /**
     * http reques put 方法封装
     * @param uri
     * @param data
     * @param header
     * @param rsDataClass
     * @return
     */
    @Override
    public ServiceRsModel<Object> sendRequestPutHttpServerHeader(String webClientBeanName, String threadPoolBeanName, String uri, Object data, Object header, Class rsDataClass){

        try{

            Future<String> rsFuture = httpWebClientService.sendRequestPutHttpServer(buildWebClientRequestModelToData(webClientBeanName, threadPoolBeanName, uri, data, null));

            String rsJason = rsFuture.get();

            return HttpResponseStatusUtils.buildHttpResponseBean(rsJason, rsDataClass);

        } catch (Exception e) {

            log.error("sendRequestPutHttpServer ia error",e);

        }

        return new ServiceRsModel().error();

    }

    /**
     * http reques get 方法封装
     * @param uri
     * @param data
     * @param rsDataClass
     * @return
     */
    @Override
    public ServiceRsModel<Object> sendRequestGetHttpServerAllParams(String webClientBeanName, String threadPoolBeanName, String uri, Object data, Class rsDataClass){

        try{

            Future<String> rsFuture = httpWebClientService.sendRequestGetHttpServer(buildWebClientRequestModelToDataMap(webClientBeanName, threadPoolBeanName, uri, data, null,true));

            String rsJason = rsFuture.get();

            return HttpResponseStatusUtils.buildHttpResponseBean(rsJason, rsDataClass);

        } catch (Exception e) {

            log.error("sendRequestGetHttpServer ia error",e);

        }

        return new ServiceRsModel().error();

    }

    /**
     * http reques get 方法封装
     * @param uri
     * @param data
     * @param rsDataClass
     * @return
     */
    @Override
    public ServiceRsModel<Object> sendRequestGetHttpServer(String webClientBeanName, String threadPoolBeanName, String uri, Object data, Class rsDataClass){

        try{

            Future<String> rsFuture = httpWebClientService.sendRequestGetHttpServer(buildWebClientRequestModelToDataMap(webClientBeanName, threadPoolBeanName, uri, data, null,false));

            String rsJason = rsFuture.get();

            return HttpResponseStatusUtils.buildHttpResponseBean(rsJason, rsDataClass);

        } catch (Exception e) {

            log.error("sendRequestGetHttpServer ia error",e);

        }

        return new ServiceRsModel().error();

    }

    /**
     * http reques get 返回值 不封装
     * @param uri
     * @param data
     * @return
     */
    @Override
    public ServiceRsModel<String> sendRequestGetHttpResponseStringServer(String webClientBeanName, String threadPoolBeanName, String uri, Object data){

        try{

            Future<String> rsFuture = httpWebClientService.sendRequestGetHttpNoNoneMatchServer(buildWebClientRequestModelToDataMap(webClientBeanName, threadPoolBeanName, uri, data, null,false));

            String rsJason = rsFuture.get();

            return new ServiceRsModel<String>().setRsData(rsJason);

        } catch (Exception e) {

            log.error("sendRequestGetHttpServer ia error",e);

        }

        return new ServiceRsModel<String>().error();

    }

    /**
     * http reques get 方法封装
     * @param uri
     * @param data
     * @param rsDataClass
     * @return
     */
    @Override
    public ServiceRsModel<?> sendRequestGetHttpServerForAllTypeAllParams(String webClientBeanName, String threadPoolBeanName, String uri, Object data, Class rsDataClass){

        try{

            Future<String> rsFuture = httpWebClientService.sendRequestGetHttpServer(buildWebClientRequestModelToDataMap(webClientBeanName, threadPoolBeanName, uri, data, null,true));

            String rsJason = rsFuture.get();

            return HttpResponseStatusUtils.buildHttpResponseBeanForAllType(rsJason, rsDataClass);

        } catch (Exception e) {

            log.error("sendRequestGetHttpServer ia error",e);

        }

        return new ServiceRsModel().error();

    }


    /**
     * http reques get 方法封装
     * @param uri
     * @param data
     * @param rsDataClass
     * @return
     */
    @Override
    public ServiceRsModel<?> sendRequestGetHttpServerForAllType(String webClientBeanName, String threadPoolBeanName, String uri, Object data, Class rsDataClass){

        try{

            Future<String> rsFuture = httpWebClientService.sendRequestGetHttpServer(buildWebClientRequestModelToDataMap(webClientBeanName, threadPoolBeanName, uri, data, null,false));

            String rsJason = rsFuture.get();

            return HttpResponseStatusUtils.buildHttpResponseBeanForAllType(rsJason, rsDataClass);

        } catch (Exception e) {

            log.error("sendRequestGetHttpServer ia error",e);

        }

        return new ServiceRsModel().error();

    }

    /**
     * http reques post 方法封装
     * @param uri
     * @param data
     * @param rsDataClass
     * @return
     */
    @Override
    public ServiceRsModel<?> sendRequestPostAllTypeHttpServer(String webClientBeanName, String threadPoolBeanName, String uri, Object data, Class rsDataClass){

        try{

            Future<String> rsFuture = httpWebClientService.sendRequestPostHttpServer(buildWebClientRequestModelToData(webClientBeanName, threadPoolBeanName, uri, data, null));

            String rsJason = rsFuture.get();

            return HttpResponseStatusUtils.buildHttpResponseBeanForAllType(rsJason, rsDataClass);

        } catch (Exception e) {

            log.error("sendRequestPostAllTypeHttpServer ia error",e);

        }

        return new ServiceRsModel().error();

    }

    /**
     * http reques get MultiValue方法封装
     * @param uri
     * @param data
     * @param rsDataClass
     * @return
     */
    @Override
    public ServiceRsModel<Object> sendRequestGetMultiValueMapHttpServerAllParams(String webClientBeanName, String threadPoolBeanName, String uri, Object data, Class rsDataClass){

        try{

            Future<String> rsFuture = httpWebClientService.sendRequestGetMultiValueMapHttpServer(buildWebClientRequestModelToDataMap(webClientBeanName, threadPoolBeanName, uri, data, null,true));

            String rsJason = rsFuture.get();

            return HttpResponseStatusUtils.buildHttpResponseBean(rsJason, rsDataClass);

        } catch (Exception e) {

            log.error("sendRequestGetHttpServer ia error",e);

        }

        return new ServiceRsModel().error();

    }

    /**
     * http reques get MultiValue方法封装
     * @param uri
     * @param data
     * @param rsDataClass
     * @return
     */
    @Override
    public ServiceRsModel<Object> sendRequestGetMultiValueMapHttpServer(String webClientBeanName, String threadPoolBeanName, String uri, Object data, Class rsDataClass){

        try{

            Future<String> rsFuture = httpWebClientService.sendRequestGetMultiValueMapHttpServer(buildWebClientRequestModelToDataMap(webClientBeanName, threadPoolBeanName, uri, data, null,false));

            String rsJason = rsFuture.get();

            return HttpResponseStatusUtils.buildHttpResponseBean(rsJason, rsDataClass);

        } catch (Exception e) {

            log.error("sendRequestGetHttpServer ia error",e);

        }

        return new ServiceRsModel().error();

    }

    /**
     * 上传文件方法
     * @param uri
     * @param streamUploadRequestModel
     * @param rsDataClass
     * @return
     */
    @Override
    public ServiceRsModel<Object> sendRequestStreamUploadHttpServer(String webClientBeanName, String threadPoolBeanName, String uri
            , StreamUploadRequestModel streamUploadRequestModel, Class rsDataClass){

        try{

            WebClientRequestModel webClientRequestModel = new WebClientStreamRequestModel();

            buildWebClientRequestModel(webClientRequestModel, webClientBeanName, threadPoolBeanName, uri);

//            webClientRequestModel.setHttpHeaderMap(bulidHttpHeaderMap(streamUploadRequestModel));

            Future<String> rsFuture = httpWebClientService.sendRequestStreamUploadHttpServer(webClientRequestModel, streamUploadRequestModel.getMultipartFile());

            String rsJason = rsFuture.get();

            return HttpResponseStatusUtils.buildHttpResponseBean(rsJason, rsDataClass);

        } catch (Exception e) {

            log.error("sendRequestStreamHttpServer ia error",e);

        }

        return new ServiceRsModel().error();

    }


    /**
     * 上传文件方法
     * @param uri
     * @param streamDownLoadRequestModel
     * @param rsDataClass
     * @return
     */
    @Override
    public ServiceRsModel<Object> sendRequestStreamDownLoadHttpServer(String webClientBeanName, String threadPoolBeanName, String uri
            , StreamDownLoadRequestModel streamDownLoadRequestModel, Class rsDataClass){

        try{

            WebClientStreamRequestModel webClientStreamRequestModel = new WebClientStreamRequestModel();

            buildWebClientRequestModel(webClientStreamRequestModel, webClientBeanName, threadPoolBeanName, uri);

            webClientStreamRequestModel.setMediaName(streamDownLoadRequestModel.getMediaName());

//            webClientStreamRequestModel.setHttpHeaderMap(bulidHttpHeaderMap(streamDownLoadRequestModel));

            Future<StreamDownLoadResponseData> rsFuture = httpWebClientService.sendRequestStreamDownLoadHttpServer(webClientStreamRequestModel);

            StreamDownLoadResponseData streamDownLoadResponseData = rsFuture.get();

            if(streamDownLoadResponseData.getStreamData() != null){

                return new ServiceRsModel().setRsData(streamDownLoadResponseData);

            }else{

                return HttpResponseStatusUtils.buildHttpResponseBean(streamDownLoadResponseData.getResponseErrorMessages(), rsDataClass);

            }

        } catch (Exception e) {

            log.error("sendRequestStreamHttpServer ia error",e);

        }

        return new ServiceRsModel().error();

    }

    /**
     * http reques delete 方法封装
     * @param uri
     * @param data
     * @param rsDataClass
     * @return
     */
    @Override
    public ServiceRsModel<Object> sendRequestDeleteHttpServer(String webClientBeanName, String threadPoolBeanName, String uri, Object data, Class rsDataClass){

        try{

            Future<String> rsFuture = httpWebClientService.sendRequestDeleteHttpServer(buildWebClientRequestModelToData(webClientBeanName, threadPoolBeanName, uri, data, null));

            String rsJason = rsFuture.get();

            return HttpResponseStatusUtils.buildHttpResponseBean(rsJason, rsDataClass);

        } catch (Exception e) {

            log.error("sendRequestDeleteHttpServer ia error",e);

        }

        return new ServiceRsModel().error();

    }


    /**
     * 组装httpHeader对象
     * @param data
     * @return
     */
    private Map<String, String> bulidHttpHeaderMap(Map<String, String> data){

//        if(data instanceof MatrixRequestModel) {
//
//            Map<String, String> httpHeaderMaps = ((MatrixRequestModel) data).getHttpRequestHeaderMaps();
//
//            String useAccessToken = getUseAccessToken(httpHeaderMaps.get(MatrixHttpConstants.MATRIX_HTTP_HEADER_TOKEN_TAG));
//
//            if(StringUtils.isNotEmpty(useAccessToken)){
//
//                httpHeaderMaps.put(MatrixHttpConstants.MATRIX_HTTP_HEADER_TOKEN_TAG, useAccessToken);
//
//            }
//
//            return httpHeaderMaps;
//
//        }

        if(data != null){

            return  data;

        }else{

            return new HashMap<String, String>();

        }

    }

    /**
     * 获得token
     * @param useAccessToken
     * @return
     */
    private String getUseAccessToken(String useAccessToken){

        return  (StringUtils.isNotEmpty(useAccessToken) ? "Bearer " + useAccessToken  : null);

    }

    private WebClientRequestModel buildWebClientRequestModelToData(String webClientBeanName, String threadPoolBeanName, String uri, Object data, Map<String, String> headerMap){

        WebClientRequestModel webClientRequestModel = new WebClientRequestModel();

        buildWebClientRequestModel(webClientRequestModel, webClientBeanName, threadPoolBeanName, uri);

        webClientRequestModel.setData(GJonUtilNamingPolicy.toJson(data));

        webClientRequestModel.setHttpHeaderMap(bulidHttpHeaderMap(headerMap));

        return webClientRequestModel;
    }


    private WebClientRequestModel buildWebClientRequestModelToDataMap(String webClientBeanName, String threadPoolBeanName, String uri, Object data, Map<String, String> headerMap, boolean isParent) throws IllegalAccessException {

        WebClientRequestModel webClientRequestModel = new WebClientRequestModel();

        if(data instanceof Map){

            webClientRequestModel.setDataMap((Map)data);

        }else{

            if(isParent){

                webClientRequestModel.setDataMap(BeanMapUtil.beanToMapNotNull(data,false));

            }else{

                webClientRequestModel.setDataMap(BeanMapUtil.beanToMapNotParentNotNull(data,false));

            }

        }

        buildWebClientRequestModel(webClientRequestModel, webClientBeanName, threadPoolBeanName, uri);

        webClientRequestModel.setHttpHeaderMap(bulidHttpHeaderMap(headerMap));

        return webClientRequestModel;

    }

    private void buildWebClientRequestModel(WebClientRequestModel webClientRequestModel, String webClientBeanName, String threadPoolBeanName, String uri){

        webClientRequestModel.setWebClientBeanName(webClientBeanName);

        webClientRequestModel.setThreadPoolBeanName(threadPoolBeanName);

        webClientRequestModel.setPath(uri);

    }
}
