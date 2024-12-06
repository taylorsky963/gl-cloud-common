package com.gl.myparadise.common.bean.model;

import com.gl.myparadise.common.bean.validation.group.UpdateDataValiPKCode;
import com.gl.myparadise.common.bean.validation.group.UpdateDataValiUser;

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

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    public String getDataPkCode() {
        return dataPkCode;
    }

    public void setDataPkCode(String dataPkCode) {
        this.dataPkCode = dataPkCode;
    }


    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }


    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public Long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    public Long getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(Long updateUserId) {
        this.updateUserId = updateUserId;
    }

    public String getUpdateUserName() {
        return updateUserName;
    }

    public void setUpdateUserName(String updateUserName) {
        this.updateUserName = updateUserName;
    }

}