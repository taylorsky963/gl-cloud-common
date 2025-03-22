package com.gl.myparadise.common.bean.report.user;

import com.gl.myparadise.common.bean.report.BaseReport;
import lombok.Data;

import java.util.Date;

@Data
public class MemberVipReport extends BaseReport {

    private Long memberInfoId;
    private String dataPkCode;
    private Integer beforeVip;
    private Integer vip;
}
