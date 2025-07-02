package com.gl.myparadise.common.bean.report.user;

import com.gl.myparadise.common.bean.report.fund.MemberFinGatherReport;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSync implements Serializable {

    private static final long serialVersionUID = 2595682138324529622L;

    // ================================ memberInfo ================================
    private Long memberInfoId;
    private String memberAccount;
    private String memberPassword;
    private String memberName;
    private String iProfilePicture;
    private String email;
    private String mobileNumber;
    private String regIpAddr;
    private String regDevice;
    private String regResource;
    private String regClientVersion;
    private Long agentId;
    private String agentAccount;
    private String agentInviteCode;
    private Timestamp birthdayTime;
    private String realName;

    // ================================ MemberWebProperty ================================
    private Long memberStatusId;

    private String memberStatus;

    private String emailValid;

    private String mobileValid;

    private Timestamp emailValidTime;

    private Timestamp mobileValidTime;

    private String isDelete;

    private String createUserName;

    private Long createUserId;

    private Timestamp createTime;

    private String updateUserName;

    private Long updateUserId;

    private Timestamp updateTime;

    private String dataPkCode;

    // ================================ memberTpLogin ================================
    private Long memberTpLoginId;

    private String loginType;
    private String tpId;
    private String tpAccount;
    // ================================ MemberWebProperty ================================
    /**
     *  自增id
     */
    private Long memberWebPropertyId;

    /**
     *  注册语言id
     */
    private Long regLangInfoId;
    /**
     *  注册货币表pkcode
     */
    private String regCurDataPkCode;
    /**
     *  当前语言id
     */
    private Long currLangInfoId;
    /**
     *  当前货币表pkcode
     */
    private String currCurDataPkCode;

    private MemberFinGatherReport funPHP;

    private MemberFinGatherReport funCNY;

    private MemberFinGatherReport funVND;

    private MemberFinGatherReport funUSD;

    // ==============================MemberRisk==========================================

    /**
     * 会员风控等级
     */
    private List<String> risk;

    // ==============================MemberKycStatus=====================================

    /**
     * 会员kyc状态
     */
    private String kycStatus;

    /**
     * 會員積分 vip等級
     */
    private Integer vipLevel;
    private BigDecimal point;
}