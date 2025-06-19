package com.gl.myparadise.common.bean.report.user;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

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

    /**
     * 首存金额
     */
    private java.math.BigDecimal firstDepositAmount;
    /**
     * 首存时间
     */
    private Timestamp firstDepositTime;
    /**
     * 最后一次存款金额
     */
    private java.math.BigDecimal lastDepositAmount;
    /**
     * 最后一次存款时间
     */
    private Timestamp lastDepositTime;
    /**
     * 总存款次数
     */
    private Integer totalDepositCount;
    /**
     * 总存款金额
     */
    private java.math.BigDecimal totalDepositAmount;
    /**
     * 首次提款金额
     */
    private java.math.BigDecimal firstWithdrawAmount;
    /**
     * 首次提款时间
     */
    private Timestamp firstWithdrawTime;
    /**
     * 最后一次提款金额
     */
    private java.math.BigDecimal lastWithdrawAmount;
    /**
     * 最后一次提款时间
     */
    private Timestamp lastWithdrawTime;
    /**
     * 总提款次数
     */
    private Integer totalWithdrawCount;
    /**
     * 总提款金额
     */
    private java.math.BigDecimal totalWithdrawAmount;

}