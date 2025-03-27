package com.gl.myparadise.common.bean.report.user;

import com.gl.myparadise.common.bean.report.BaseReport;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@ToString(callSuper = true)
public class DailyUserBettingReport extends BaseReport {
    private Integer betCount;
    private BigDecimal totalBetFlowAmount;
    private BigDecimal totalBetAmount;
    private BigDecimal totalSettleAmount;

    private Long memberInfoId;
    private Integer gameType;
    private String gamePlatformCode;
    private String curDataPkCode;
    private String day;
}
