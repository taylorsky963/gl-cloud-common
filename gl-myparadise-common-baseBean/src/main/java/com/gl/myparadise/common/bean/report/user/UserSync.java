package com.gl.myparadise.common.bean.report.user;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSync implements Serializable {

    // ================================ 基本身份信息 ================================
    private Integer id;
    private String username;
    private String nickName;
    private String reallyName;
    private Integer sex;
    private String birthday;
    private String country;

    // ================================ 聯絡信息 ================================
    private String email;
    private String telephone;
    private String telArea;

    // ================================ 賬戶狀態 ================================
    private Integer status;
    private String remark;
    private Integer clientType;
    private BigDecimal balance;
    private Integer levelStatus;
    private Long lastUpdate;
    private Long logoutTime;

    // ================================= VIP =================================
    private Integer vipLevel;
    private Long vipLevelTime;
    private Integer preVipLevel;

    // ================================ 註冊 ================================
    private String registerIp;
    private Long registerDate;
    private String registerDomain;
    private String registerDeviceId;

    // ================================ 登入 ================================
    private Long loginTime;
    private String loginIp;
    private Integer loginType;
    private Integer loginClientType;
}