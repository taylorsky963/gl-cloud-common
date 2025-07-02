package com.gl.myparadise.common.webclient.model.request.stream;

import com.gl.myparadise.common.webclient.model.request.WebClientRequestModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Vincent
 * @description:
 * @className: WebClientStreamRequestModel
 * @projectName: gl-cloud-base
 * @date 2025-04-02 - 01:05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@ToString(callSuper = true)
public class WebClientStreamRequestModel extends WebClientRequestModel {

    static final long serialVersionUID = 7485488428423249849L;

    private String mediaName;

}
