package com.gl.myparadise.common.webclient.model.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: HttpResponseErrorModel
 * @ProjectName: gi-matrix-parent
 * @Date 2023-06-26 - 3:33 p.m.
 */
@Data
@NoArgsConstructor
@ToString(callSuper = true)
public class HttpResponseErrorData implements Serializable {

    private static final long serialVersionUID = 2229480052206578147L;

    private String responseStatus = "error";

    private String responseErrorCode;

    private String responseErrorMessages;

}