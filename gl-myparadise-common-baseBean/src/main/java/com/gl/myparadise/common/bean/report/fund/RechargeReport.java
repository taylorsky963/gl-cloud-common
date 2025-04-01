package com.gl.myparadise.common.bean.report.fund;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.gl.myparadise.common.bean.report.BaseReport;
import com.gl.myparadise.common.bean.validation.group.UpdateDataValiUser;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Getter
@Setter
public class RechargeReport extends BaseReport {

    /**
     *  数据主键ID
     */
    private Long payOrderId;
    /**
     *  会员ID
     */
    private Long memberInfoId;
    /**
     *  会员账号
     */
    private String memberAccount;
    /**
     *  订单编号
     */
    private String orderNumber;
    /**
     *  外部系统订单编号
     */
    private String externalNumber;
    /**
     *  支付渠道ID
     */
    private Long paymentChannelId;
    /**
     *  支付渠道名称
     */
    private String paymentChannelName;
    /**
     *  支付渠道编号
     */
    private String paymentChannelCode;
    /**
     *  支付分组编号
     */
    private String paymentGroup;
    /**
     *  支付分组名
     */
    private String paymentGroupName;
    /**
     *  支付类型编号
     */
    private String paymentModel;
    /**
     *  支付类型名
     */
    private String paymentModelName;
    /**
     *  货币ID
     */
    private Long curConfigId;
    /**
     *  货币名称
     */
    private String curSign;
    /**
     *  订单金额
     */
    private java.math.BigDecimal orderAmount;
    /**
     *  支付银行账户名
     */
    private String PayBankAccount;
    /**
     *  支付银行账号
     */
    private String payBankNumber;
    /**
     *  支付银行名称
     */
    private String payBankName;
    /**
     *  USDT地址
     */
    private String usdtAccount;
    /**
     *  USDT汇率
     */
    private java.math.BigDecimal usdtRate;
    /**
     *  支付金额类型
     */
    private String payAmountType;
    /**
     *  支付金额
     */
    private java.math.BigDecimal payAmount;
    /**
     *  支付成功实践
     */
    private Timestamp successTime;
    /**
     *  支付成功操作人
     */
    private String successOperator;
    /**
     *  支付订单下单渠道：PC、H5
     */
    private String orderSource;
    /**
     *  订单备注信息
     */
    private String remark;
    /**
     *  支付订单状态
     */
    private String status;

    private String dataPkCode;

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

    private String curDataPkCode;
}
