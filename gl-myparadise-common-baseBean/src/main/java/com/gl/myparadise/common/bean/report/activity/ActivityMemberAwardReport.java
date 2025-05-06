package com.gl.myparadise.common.bean.report.activity;

import com.gl.myparadise.common.bean.report.BaseReport;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ActivityMemberAwardReport extends BaseReport {
    private Integer awardType;
    private Integer prizeId;
    private String prizeName;
    private String imgUrl;
    private Double price;
    private Integer status;
    private Date createTime;
    private Date claimTime;
    private Date expireTime;
    private Object extraInfo;
    private String userAccount;
    private String telephone;
    private String activityDataPkCode;
    private String actName;
    private String actTemplate;
    private String username;
    private Long parentId;
    private String parentName;
    private String remark;
    private Integer desireMultiple;
    private String sender;
    private String tradeId;
    private String withdrewOrderId;
    private String applyRemark;
    private Integer validDays;
    private String firstVerifyRemark;
    private String secondVerifyRemark;
    private String userTag;
    private String coinCode;
    private String coinDataPkCode;
    private Integer subType;
    private Long memberInfoId;
}