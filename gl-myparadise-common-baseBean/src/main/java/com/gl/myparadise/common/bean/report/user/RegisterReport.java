package com.gl.myparadise.common.bean.report.user;

import com.fasterxml.jackson.databind.JsonNode;
import com.gl.myparadise.common.bean.model.ReportEvent;
import com.gl.myparadise.common.bean.report.BaseReport;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterReport extends BaseReport {

    /**
     * 用户ID
     */
    private Integer uid;

    /**
     * 邀请人邀请码
     */
    private String inviterCode;

    public static RegisterReport parseObject(final JsonNode jsonNode) {
        RegisterReport report = new RegisterReport();
        if (jsonNode == null) {
            return report;
        }

        setBaseField(jsonNode, report);

        JsonNode eventObj = jsonNode.get(String.valueOf(ReportEvent.Register.value()));
        if (eventObj == null) {
            return report;
        }

        if (eventObj.has("uid")) {
            report.setUid(eventObj.get("uid").intValue());
        }

        if (eventObj.has("inviterCode")) {
            report.setInviterCode(eventObj.get("inviterCode").asText());
        }

        return report;
    }

}