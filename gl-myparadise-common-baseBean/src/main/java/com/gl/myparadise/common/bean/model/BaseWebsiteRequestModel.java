package com.gl.myparadise.common.bean.model;

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
    private String platformDataPkCode;

    /**
     * 货币code
     */
    private String curDataPkCode;

    /**
     * 语言参数code
     */
    private String langSign;

    /**
     * 设备类型
     */
    private String deviceType;

    /**
     * 平台id
     */
    private Long platformInfoId;

    /**
     * 设备id
     */
    private String deviceId;

}
