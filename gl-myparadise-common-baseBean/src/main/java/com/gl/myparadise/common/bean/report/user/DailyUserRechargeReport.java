package com.gl.myparadise.common.bean.report.user;

import com.gl.myparadise.common.bean.report.BaseReport;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@ToString(callSuper = true)
public class DailyUserRechargeReport extends BaseReport {

    private Integer rechargeCount;
    private BigDecimal totalOrderAmount;
    private BigDecimal totalPayAmount;

    private Long memberInfoId;
    private String paymentChannelCode;
    private String curSign;
    private String curDataPkCode;
    private String day;
}
