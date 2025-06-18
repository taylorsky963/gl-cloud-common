package com.gl.myparadise.common.bean.report.user;

import com.gl.myparadise.common.bean.report.BaseReport;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@ToString(callSuper = true)
public class SeamlessGameTransactionReport extends BaseReport {
    private String id;
    private String action;  //bet , endround , credit , debit , rollout , rollin , takeall , refound
    private String platformCode;
    private String userAccount; //平台帳戶名稱
    private String gameType;
    private String gameCode;
    private String username; //遊戲帳號
    private String currency;
    private String transId;
    private BigDecimal amount;
    private BigDecimal before; //交易前餘額
    private BigDecimal balance; //交易后餘額
    private Date eventTime;  //下注時間
    private Date createTime; //交易開始時間
    private Date endTime; //交易結束時間
    private String status; //success 只有成功
    private List<Map<String,Object>> itemData = new ArrayList<>();  //放注單交易信息
}
