package com.gl.myparadise.common.bean.report.user;

import com.gl.myparadise.common.bean.report.BaseReport;
import lombok.Data;

import java.util.Date;

@Data
public class UserWalletReport extends BaseReport {

    private String userAccount;
    private String curDataPkCode;
    private String curSign;
    private Long memberInfoId;
    private Date createTime;

}
