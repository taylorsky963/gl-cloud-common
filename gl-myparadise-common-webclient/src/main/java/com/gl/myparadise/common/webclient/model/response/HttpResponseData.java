package com.gl.myparadise.common.webclient.model.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: MatrixResponseData
 * @ProjectName: gl-matrix-parent
 * @Date 2023-07-27 - 8:01 p.m.
 */
@Data
@NoArgsConstructor
@ToString(callSuper = true)
public class HttpResponseData implements Serializable {

    private static final long serialVersionUID = 7920556578653659423L;

    private String responseStatus;

    private String responseErrorCode;

    private String responseErrorMessages;


}