package com.gl.myparadise.common.bean.report.system;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class SystemOperationLogReport implements Serializable {

    private Integer jobId;

    private String deptAndJob;

    private Integer adminId;

    private Long operationTime;

    private String operationMenu;

    private String menuId;

    private String operator;

    private String url;

    private String param;

    private String result;

}
