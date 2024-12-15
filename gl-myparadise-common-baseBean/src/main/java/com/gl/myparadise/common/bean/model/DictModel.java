package com.gl.myparadise.common.bean.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: DictModel
 * @ProjectName: myparadise-common
 * @Date 2023-04-17 - 20:29
 */
@Data
public class DictModel implements Serializable {


    private static final long serialVersionUID = 6231290164795695534L;

    private String groupCode;
    private String id;
    private String itemCode;
    private String itemName;
    private String itemOrderIndex;
    private String parentCode;

}
