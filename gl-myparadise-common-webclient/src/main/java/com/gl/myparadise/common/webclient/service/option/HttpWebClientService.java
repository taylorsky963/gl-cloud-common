package com.gl.myparadise.common.webclient.service.option;

import com.gl.myparadise.common.webclient.model.request.WebClientRequestModel;
import com.gl.myparadise.common.webclient.model.request.stream.WebClientStreamRequestModel;
import com.gl.myparadise.common.webclient.model.response.stream.StreamDownLoadResponseData;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.Future;

public interface HttpWebClientService {


    Future<String> sendRequestPostHttpServer(WebClientRequestModel webClientRequestModel);

    Future<String> sendRequestPostFormHttpServer(WebClientRequestModel webClientRequestModel);

    Future<String> sendRequestPutHttpServer(WebClientRequestModel webClientRequestModel);

    Future<String> sendRequestGetMultiValueMapHttpServer(WebClientRequestModel webClientRequestModel);

    Future<String> sendRequestGetHttpServer(WebClientRequestModel webClientRequestModel);

    Future<String> sendRequestGetHttpNoNoneMatchServer(WebClientRequestModel webClientRequestModel);

    Future<String> sendRequestStreamUploadHttpServer(WebClientRequestModel webClientRequestModel, MultipartFile multipartFile);

    Future<StreamDownLoadResponseData> sendRequestStreamDownLoadHttpServer(WebClientStreamRequestModel webClientStreamRequestModel);

    Future<String> sendRequestDeleteHttpServer(WebClientRequestModel webClientRequestModel);
}
