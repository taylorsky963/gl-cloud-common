package com.gl.myparadise.common.bean.report.fund;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ActivityBonusReport extends FundActivityBalanceReport {

    private Date awardTime;
    private Date expireTime;
}