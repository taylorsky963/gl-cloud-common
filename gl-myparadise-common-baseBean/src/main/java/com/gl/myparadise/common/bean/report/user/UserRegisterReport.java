package com.gl.myparadise.common.bean.report.user;

import com.gl.myparadise.common.bean.report.BaseReport;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Timestamp;

/**
 * 用户注册上报数据
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserRegisterReport extends BaseReport {

    /**
     * 会员ID
     */
    private Long memberInfoId;

    /**
     * 用户名
     */
    private String memberAccount;

    /**
     * 注册语言id
     */
    private Long regLangInfoId;

    /**
     * 注册货币表
     */
    private String regCurDataPkCode;

    /**
     * 創建時間
     */
    private Timestamp createTime;

    /**
     * 代理商ID
     */
    private Long agentId;
    
    /**
     * 代理商帳號
     */
    private String agentAccount;
}
