package com.gl.myparadise.common.bean.report.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserBaseReport implements Serializable {

    /**
     * 事件类型
     */
    private Integer event;

    /**
     * 时间戳
     */
    private Long timestamp;

}