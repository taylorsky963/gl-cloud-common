package com.gl.myparadise.common.webclient.model.request.stream;

import com.gl.myparadise.common.webclient.model.request.HttpRequestModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: LoginRequestModel
 * @ProjectName: gi-matrix-parent
 * @Date 2023-06-24 - 5:08 p.m.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@ToString(callSuper = true)
public class StreamUploadRequestModel extends HttpRequestModel {

    private static final long serialVersionUID = -3750686850409948647L;

    private String filename;

    @NotNull
    private MultipartFile multipartFile;

}
