package com.gl.myparadise.common.bean.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author Vincent
 * @description:
 * @className: BaseWebsiteRequestModel
 * @projectName: gl-cloud-base
 * @date 2025-02-18 - 02:39
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@ToString(callSuper = true)
public class BaseWebsiteRequestModel extends BaseEntity {

    private static final long serialVersionUID = 6151482436627873392L;
    /**
     * 游戏线路code
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String platformDataPkCode;

    /**
     * 货币code
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String curDataPkCode;

    /**
     * 语言参数code
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String langSign;

    /**
     * 设备类型
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String deviceType;

    /**
     * 平台id
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long platformInfoId;

    /**
     * 设备id
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String deviceId;

    /**
     * 语言id
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long langInfoId;

    /**
     * 语言code
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long langDataPkCode;

}
