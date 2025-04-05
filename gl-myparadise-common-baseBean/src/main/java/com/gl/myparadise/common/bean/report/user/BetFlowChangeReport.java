package com.gl.myparadise.common.bean.report.user;

import com.gl.myparadise.common.bean.report.BaseReport;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@ToString(callSuper = true)
public class BetFlowChangeReport extends BaseReport {
    // 唯一主键
    private String dataPkCode;

    /**
     * 自增id
     */
    private Long betflowEffectLogId;
    /**
     * 帐号id
     */
    private Long memberInfoId;
    /**
     * 账号
     */
    private String memberAccount;
    /**
     * 货币ID
     */
    private Long curConfigId;
    /**
     * 币版本标识
     */
    private String curSign;
    /**
     * 流水類型：1=充值, 2=優惠, 3=人工
     */
    private Integer flowType;
    /**
     * 金额
     */
    private java.math.BigDecimal amount;
    /**
     * 流水倍数
     */
    private java.math.BigDecimal magnificationFactor;
    /**
     * 单笔流水需求
     */
    private java.math.BigDecimal singleBetDesire;
    /**
     * 已完成流水
     */
    private java.math.BigDecimal betCompleted;
    /**
     * 总流水需求
     */
    private java.math.BigDecimal totalBetCompleted;
    /**
     * 变动前所需流水
     */
    private java.math.BigDecimal beforeBetCompleted;
    /**
     * 变动后所需流水
     */
    private java.math.BigDecimal afterBetCompleted;
    /**
     * 0: 已清除，1: 正常，2: 清除审核中，3: 恢复审核中
     */
    private String status;
    /**
     * 备注
     */
    private String remark;


    private String isDelete;

    private String createUserName;


    private Long createUserId;

    /**
     * 创建时间
     */

    private Timestamp createTime;


    private String updateUserName;


    private Long updateUserId;

    /**
     * 修改时间
     */
    private Timestamp updateTime;
} 