package com.gl.myparadise.common.bean.report.fund;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.gl.myparadise.common.bean.report.BaseReport;
import com.gl.myparadise.common.bean.validation.group.UpdateDataValiPKCode;
import com.gl.myparadise.common.bean.validation.group.UpdateDataValiUser;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Getter
@Setter
public class WithdrawReport extends BaseReport {
    /**
     *  主键ID
     */
    private Long withdrawOrderId;
    /**
     *  提款渠道类型：Gcash、PayMaya、GrabPay、USDT、Bank
     */
    private String withdrawType;
    /**
     *  货币ID
     */
    private Long curConfigId;
    /**
     *  货币编号
     */
    private String curSign;
    /**
     *  提款金额
     */
    private java.math.BigDecimal amount;
    /**
     *  会员最终到账提款金额，扣除手续费之后的金额
     */
    private java.math.BigDecimal realAmount;
    /**
     * 订单编号
     */
    private String orderNumber;
    /**
     *  会员ID
     */
    private Long memberInfoId;
    /**
     *  会员账号
     */
    private String memberAccount;
    /**
     *  会员银行卡提现方式对应的银行账户名
     */
    private String bankAccount;
    /**
     *  会员银行卡提现方式对应的银行卡号
     */
    private String bankNumber;
    /**
     *  会员银行卡提现方式对应的银行名称
     */
    private String bankName;
    /**
     *  会员USDT提现对应的USDT地址
     */
    private String usdtAccount;
    /**
     *  会员USDT提现对应的USDT汇率
     */
    private java.math.BigDecimal usdtRate;
    /**
     *  会员USDT提现对应的USDT链类型
     */
    private String usdtType;
    /**
     *  会员钱包提现对应的钱包账号
     */
    private String walletAccount;
    /**
     *  提现手续费，
     */
    private java.math.BigDecimal withdrawFee;
    /**
     *  风控审核时间
     */
    private Timestamp riskCheckTime;
    /**
     *  风控审核人
     */
    private String riskCheckOperator;
    /**
     *  风控审核备注
     */
    private String riskCheckRemark;
    /**
     *  提款成功时间
     */
    private Timestamp successTime;
    /**
     *  提款成功操作人
     */
    private String successOperator;
    /**
     *  代付出款渠道ID
     */
    private Long channelId;
    /**
     *  代付出款渠道名称
     */
    private String channelName;
    /**
     *  状态
     */
    private String status;

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

    /**
     * 系统返回的数据唯一id
     */
    private String dataPkCode;

    private String curDataPkCode;
}
