package com.gl.myparadise.common.bean.report.fund;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FundBaseReport implements Serializable {

    /**
     * 唯一ID
     */
    private String transactionId;

    /**
     * 事件类型
     */
    private Integer event;

    /**
     * 时间戳
     */
    private Long timestamp;
    /**
     * 该对象会直接直接返回给调用端
     */
    private Object attachment;

//    private Long ttl;
}