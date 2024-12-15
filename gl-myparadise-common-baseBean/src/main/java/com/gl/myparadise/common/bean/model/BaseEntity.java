package com.gl.myparadise.common.bean.model;

import com.gl.myparadise.common.bean.validation.group.UpdateDataValiPKCode;
import com.gl.myparadise.common.bean.validation.group.UpdateDataValiUser;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: BaseEntity
 * @ProjectName: gl-matrix-parent
 * @Date 2023-07-20 - 10:32 p.m.
 */
@Data
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = 8645242011534183215L;
    private String isDelete;

    private String createUserName;


    private Long createUserId;

    /**
     * 创建时间
     */
    private Timestamp createTime;

    @NotNull(groups= UpdateDataValiUser.class)
    private String updateUserName;

    private Long updateUserId;

    /**
     * 修改时间
     */
    private Timestamp updateTime;

    /**
     * 系统返回的数据唯一id
     */
    @NotNull(groups= {UpdateDataValiPKCode.class, UpdateDataValiUser.class})
    private String dataPkCode;

}