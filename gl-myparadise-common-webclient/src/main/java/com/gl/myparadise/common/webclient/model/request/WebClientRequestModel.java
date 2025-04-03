package com.gl.myparadise.common.webclient.model.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Map;

/**
 * @author Vincent
 * @description:
 * @className: WebCilentSendModel
 * @projectName: gl-cloud-base
 * @date 2025-04-02 - 00:35
 */
@Data
@NoArgsConstructor
@ToString(callSuper = true)
public class WebClientRequestModel implements Serializable {

    private static final long serialVersionUID = 8352407257216158924L;

    private String webClientBeanName;

    private String threadPoolBeanName;

    private String path;

    private String data;

    private Map<String, String> httpHeaderMap;

    private Map<String, Object> dataMap;

}
