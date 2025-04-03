package com.gl.myparadise.common.webclient.service.option;

import com.gl.myparadise.common.webclient.model.request.WebClientRequestModel;
import com.gl.myparadise.common.webclient.model.request.stream.WebClientStreamRequestModel;
import com.gl.myparadise.common.webclient.model.response.stream.StreamDownLoadResponseData;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.Future;

public interface HttpWebClientService {


    Future<String> sendRequestPostMatrixServer(WebClientRequestModel webClientRequestModel);

    Future<String> sendRequestPutMatrixServer(WebClientRequestModel webClientRequestModel);

    Future<String> sendRequestGetMultiValueMapMatrixServer(WebClientRequestModel webClientRequestModel);

    Future<String> sendRequestGetMatrixServer(WebClientRequestModel webClientRequestModel);

    Future<String> sendRequestStreamUploadMatrixServer(WebClientRequestModel webClientRequestModel, MultipartFile multipartFile);

    Future<StreamDownLoadResponseData> sendRequestStreamDownLoadMatrixServer(WebClientStreamRequestModel webClientStreamRequestModel);

    Future<String> sendRequestDeleteMatrixServer(WebClientRequestModel webClientRequestModel);
}
