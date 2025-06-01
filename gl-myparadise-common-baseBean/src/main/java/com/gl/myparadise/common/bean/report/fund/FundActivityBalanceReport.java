package com.gl.myparadise.common.bean.report.fund;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class FundActivityBalanceReport extends FundBaseReport {
    private String memberAccount;

    private Long memberInfoId;

    private String activityDataPkCode;

    private BigDecimal amount;

    private Integer flowMultiple;

    private String remark;

    private Integer type;

    private String coinCode;

    private String coinDataPkCode;

    private Integer awardType;

    private BigDecimal betFlowAmount;

    private Long agentId;
}
