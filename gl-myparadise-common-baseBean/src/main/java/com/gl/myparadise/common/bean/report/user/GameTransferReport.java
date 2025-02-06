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
public class GameTransferReport extends BaseReport {
    private String username;//游戏平台账号
    private String userAccount;//会员账号
    private String gamePlatform;//平台编号code
    private String gameType;//游戏类型
    private String currency;//游戏币种
    private BigDecimal transferamount;//转账金额
    private String transferstatus;//转账状态: 1=Deposit;2=Withdraw
    private Date createTime;//转账时间
}

//
//  `id` bigint(20) NOT NULL AUTO_INCREMENT,
//  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
//        `user_account` varchar(25) DEFAULT NULL,
//  `platform_key` varchar(20) DEFAULT NULL COMMENT '平台唯一标识',
//        `game_platform` varchar(10) NOT NULL COMMENT '游戏平台编号',
//        `game_type` varchar(2) DEFAULT NULL,
//  `platform_name` varchar(255) DEFAULT NULL COMMENT '游戏平台名称',
//        `username` varchar(50) NOT NULL COMMENT '用户名',
//        `pwd` varchar(100) DEFAULT NULL,
//  `password` varchar(100) DEFAULT '' COMMENT '密码',
//        `secret_key` varchar(128) DEFAULT NULL,
//  `has_money` varchar(1) DEFAULT NULL,
//  `status` char(2) NOT NULL COMMENT '状态，0-禁用，1-正常',
//        `balance` decimal(18,2) DEFAULT NULL COMMENT '用户余额',
//        `total_bet_flow` decimal(18,2) DEFAULT NULL COMMENT '累积有效流水',
//        `total_deposit` decimal(18,2) DEFAULT NULL COMMENT '累积向平台转账',
//        `total_withdraw` decimal(18,2) DEFAULT NULL COMMENT '累积从此平台转账',
//        `create_time` datetime NOT NULL COMMENT '创建时间',
//        `update_time` datetime DEFAULT NULL COMMENT '更新时间',
//        `create_admin` varchar(25) DEFAULT NULL COMMENT '创建人',
//        `update_admin` varchar(25) DEFAULT NULL COMMENT '更新人',