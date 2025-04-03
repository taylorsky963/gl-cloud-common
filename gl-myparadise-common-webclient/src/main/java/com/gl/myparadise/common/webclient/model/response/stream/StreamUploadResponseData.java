package com.gl.myparadise.common.webclient.model.response.stream;

import com.gl.myparadise.common.webclient.model.response.HttpResponseData;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class StreamUploadResponseData extends HttpResponseData {

    private static final long serialVersionUID = 6273313602921758009L;
    /**
     * 檔案url
     */
    private String contentUri;

    public StreamUploadResponseData(String contentUri) {
        this.contentUri = contentUri;
    }

    public StreamUploadResponseData() {

    }

}
