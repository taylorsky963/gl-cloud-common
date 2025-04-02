package com.gl.myparadise.common.bean.report.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.gl.myparadise.common.bean.report.BaseReport;
import com.gl.myparadise.common.bean.validation.group.UpdateDataValiPKCode;
import com.gl.myparadise.common.bean.validation.group.UpdateDataValiUser;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@ToString(callSuper = true)
public class BalanceChangeReport extends BaseReport {
    // 唯一主键
    private String dataPkCode;
    /**
     *  帐号id
     */
    private Long memberInfoId;
    /**
     *  账号
     */
    private String memberAccount;
    /**
     *  货币ID
     */
    private Long curConfigId;
    /**
     *  币版本标识
     */
    private String curSign;
    /**
     *  交易订单号
     */
    private String transNo;
    /**
     *  交易类型
     */
    private Integer transactionType;
    /**
     *  子类型id
     */
    private Integer subTypeId;
    /**
     *  子类型
     */
    private String subType;
    /**
     *  金额
     */
    private java.math.BigDecimal amount;
    /**
     *  变动前金额
     */
    private java.math.BigDecimal beforeAmount;
    /**
     *  变动后金额
     */
    private java.math.BigDecimal afterAmount;
    /**
     *  手續費金額
     */
    private java.math.BigDecimal feeAmount;
    /**
     *  实际到账金额
     */
    private java.math.BigDecimal actualAmount;
    /**
     *  增加还是减少，1增加，-1减少
     */
    private Integer transIo;
    /**
     *  备注
     */
    private String remark;

    @JsonIgnore
    private String isDelete;

    private String createUserName;
    private Long createUserId;
    private Timestamp createTime;
    private String updateUserName;
    private Long updateUserId;
    private Timestamp updateTime;

} 