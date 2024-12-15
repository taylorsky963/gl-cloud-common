package com.gl.myparadise.common.bean.report;

import com.fasterxml.jackson.databind.JsonNode;
import com.gl.myparadise.common.bean.report.user.RegisterReport;
import lombok.Getter;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseReport implements Serializable {

    @Getter
    private final Map<String, Object> msg = new HashMap<>();

    public Object get(String k) {
        return this.msg.get(k);
    }

    public void set(String k, Object v) {
        this.msg.put(k, v);
    }

    public void setIndexShard(Date date) {
        String shard = format(date, "yyyy.MM.dd");
        this.msg.put("index", shard);
    }

    public static String format(Date date, String pattern) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.format(date);
    }

    public void setIp(String ip) {
        this.msg.put("ip", ip);
    }

    public void setCoin(String coin) {
        this.msg.put("coin", coin);
    }

    public void setUid(Integer uid) {
        this.msg.put("uid", uid);
    }

    public static void setBaseField(JsonNode jsonNode, BaseReport report) {
        if (jsonNode.has("ip")) {
            report.setIp(jsonNode.get("ip").asText());
        }
        if (jsonNode.has("uid")) {
            report.setUid(jsonNode.get("uid").asInt());
        }
        if (jsonNode.has("coin")) {
            report.setCoin(jsonNode.get("coin").asText());
        }
    }

}