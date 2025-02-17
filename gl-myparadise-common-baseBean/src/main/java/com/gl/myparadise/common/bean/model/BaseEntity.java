package com.gl.myparadise.common.bean.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
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

    @JsonIgnore
    private String isDelete;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String createUserName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long createUserId;

    /**
     * 创建时间
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Timestamp createTime;

    @NotNull(groups= UpdateDataValiUser.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String updateUserName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long updateUserId;

    /**
     * 修改时间
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Timestamp updateTime;

    /**
     * 系统返回的数据唯一id
     */
    @NotNull(groups= {UpdateDataValiPKCode.class, UpdateDataValiUser.class})
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String dataPkCode;

}