package com.gl.myparadise.common.bean.report.user;

import com.gl.myparadise.common.bean.report.BaseReport;
import lombok.*;

import java.io.Serializable;
import java.sql.Timestamp;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberLoginLogReport extends BaseReport {

    private static final long serialVersionUID = 5766952470363745793L;

    /**
     * 登录会员id
     */
    private Long memberInfoId;

    /**
     * 登录会员账号
     */
    private String memberAccount;

    /**
     * 登录时间 精确到秒
     */
    private Timestamp memberLoginTime;

    /**
     * 登录得国家
     */
    private String LoginCountry;

    /**
     * 登录得城市名
     */
    private String LoginCity;

    /**
     * 登录得方式：例如google，facebook
     */
    private String loginMethod;

    /**
     * 登录得ip 地址
     */
    private String loginIp;

    /**
     * 登录渠道 例如：安卓，ios
     */
    private String loginChannel;

    /**
     * 登录设备id
     */
    private String deviceId;

    /**
     * 登录域名
     */
    private String loginDomain;

    /**
     * 会员所属代理id
     */
    private Long agentId;

    /**
     * 会员所属代理账号
     */
    private String agentAccount;


}