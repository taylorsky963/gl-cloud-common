package com.gl.myparadise.common.bean.report.fund;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.gl.myparadise.common.bean.validation.group.UpdateDataValiPKCode;
import com.gl.myparadise.common.bean.validation.group.UpdateDataValiUser;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Getter
@Setter
public class MemberFinGatherReport {

    /**
     * 数据ID
     */
    private Long id;
    /**
     * 会员ID
     */
    private Long memberInfoId;
    /**
     * 会员账号
     */
    private String memberAccount;
    /**
     * 货币ID
     */
    private Long curConfigId;
    /**
     * 货币名
     */
    private String curSign;
    /**
     * 货币数据编号
     */
    private String curDataPkCode;
    /**
     * 代理ID
     */
    private Long agentId;
    /**
     * 代理账号
     */
    private String agentAccount;
    /**
     * 首存金额
     */
    private java.math.BigDecimal firstDepositAmount;
    /**
     * 首存时间
     */
    private Timestamp firstDepositTime;
    /**
     * 最后一次存款金额
     */
    private java.math.BigDecimal lastDepositAmount;
    /**
     * 最后一次存款时间
     */
    private Timestamp lastDepositTime;
    /**
     * 总存款次数
     */
    private Integer totalDepositCount;
    /**
     * 总存款金额
     */
    private java.math.BigDecimal totalDepositAmount;
    /**
     * 首次提款金额
     */
    private java.math.BigDecimal firstWithdrawAmount;
    /**
     * 首次提款时间
     */
    private Timestamp firstWithdrawTime;
    /**
     * 最后一次提款金额
     */
    private java.math.BigDecimal lastWithdrawAmount;
    /**
     * 最后一次提款时间
     */
    private Timestamp lastWithdrawTime;
    /**
     * 总提款次数
     */
    private Integer totalWithdrawCount;
    /**
     * 总提款金额
     */
    private java.math.BigDecimal totalWithdrawAmount;

    private String isDelete;

    private String createUserName;

    private Long createUserId;

    private Timestamp createTime;

    private String updateUserName;

    private Long updateUserId;

    private Timestamp updateTime;

    private String dataPkCode;
}
