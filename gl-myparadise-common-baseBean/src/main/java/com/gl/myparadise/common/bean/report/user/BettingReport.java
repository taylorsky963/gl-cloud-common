package com.gl.myparadise.common.bean.report.user;

import com.gl.myparadise.common.bean.report.BaseReport;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@ToString(callSuper = true)
public class BettingReport extends BaseReport {
    private String flowId;
    private String requestId;
    private String pushId;
    private String client;
    private String username;
    private String betId;
    private String platformCode;
    private String gameType;
    private String gameCode;
    private String sportsName;
    private String leagueName;
    private String eventName;
    private String gameName;
    private BigDecimal betAmount;
    private Integer result;
    private BigDecimal settleAmount;
    private Date settleTime;
    private Date betTime;
    private String status;
    private BigDecimal betFlow;
    private BigDecimal refundAmount;
    private Date createTime;
    private String detail;
    private String itemData;
    private String curDataPkCode;
    private String memberAccount;
    private Long memberInfoId;
    private Long agentId;
    private Object childItemData;
    private Integer settleVersion;
    private Object settlementHistory;  // 二次結算歷史記錄列表
    private Object eventExtraInfo;  // 額外的賽事信息、彩票信息
}
