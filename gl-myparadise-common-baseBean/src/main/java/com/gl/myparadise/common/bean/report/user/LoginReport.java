package com.gl.myparadise.common.bean.report.user;

import com.fasterxml.jackson.databind.JsonNode;
import com.gl.myparadise.common.bean.model.ReportEvent;
import com.gl.myparadise.common.bean.report.BaseReport;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginReport extends BaseReport {

    private String userAgent;

    private Integer osType;

    private Integer appType;

    private String deviceId;

    public static LoginReport parseObject(final JsonNode jsonNode) {
        LoginReport report = new LoginReport();
        if (jsonNode == null) {
            return report;
        }

        setBaseField(jsonNode, report);

        JsonNode eventObj = jsonNode.get(String.valueOf(ReportEvent.Login.value()));

        if (eventObj == null) {
            return report;
        }
        if (eventObj.has("osType")) {
            report.setOsType(eventObj.get("osType").asInt());
        }
        if (eventObj.has("appType")) {
            report.setAppType(eventObj.get("appType").asInt());
        }
        if (eventObj.has("userAgent")) {
            report.setUserAgent(eventObj.get("userAgent").asText());
        }

        if (eventObj.has("deviceId")) {
            report.setUserAgent(eventObj.get("deviceId").asText());
        }

        return report;
    }

}