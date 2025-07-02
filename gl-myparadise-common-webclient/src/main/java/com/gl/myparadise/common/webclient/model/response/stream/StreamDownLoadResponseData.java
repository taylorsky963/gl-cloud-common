package com.gl.myparadise.common.webclient.model.response.stream;


import com.gl.myparadise.common.webclient.model.response.HttpResponseData;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@ToString(callSuper = true)
public class StreamDownLoadResponseData extends HttpResponseData {

    private static final long serialVersionUID = -7416857558253661771L;

    private byte[] streamData;

}
