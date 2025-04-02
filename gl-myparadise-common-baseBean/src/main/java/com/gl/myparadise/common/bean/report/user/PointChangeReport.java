package com.gl.myparadise.common.bean.report.user;

import com.gl.myparadise.common.bean.report.BaseReport;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

@Data
@NoArgsConstructor
@ToString(callSuper = true)
public class PointChangeReport extends BaseReport {
    // 唯一主键
    private String dataPkCode;
    /**
     *  帐号id
     */
    private Long memberInfoId;
    /**
     *  账号
     */
    private String memberAccount;
    /**
     *  交易订单号
     */
    private String transNo;
    /**
     *  交易类型
     */
    private Integer transactionType;
    /**
     *  子类型id
     */
    private Integer subTypeId;
    /**
     *  子类型
     */
    private String subType;
    /**
     *  金额
     */
    private java.math.BigDecimal point;
    /**
     *  变动前积分
     */
    private java.math.BigDecimal beforePoint;
    /**
     *  变动后积分
     */
    private java.math.BigDecimal afterPoint;
    /**
     *  增加还是减少，1增加，-1减少
     */
    private Integer transIo;
    /**
     *  备注
     */
    private String remark;

    private String createUserName;
    private Long createUserId;
    private Timestamp createTime;
    private String updateUserName;
    private Long updateUserId;
    private Timestamp updateTime;
} 