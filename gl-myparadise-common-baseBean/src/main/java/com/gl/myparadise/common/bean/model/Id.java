package com.gl.myparadise.common.bean.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.util.StringUtils;

import java.io.Serializable;

@Data
public class Id implements Serializable {

    private String id;

    public Long asLong() {
        return StringUtils.isEmpty(id) ? null : Long.parseLong(id);
    }
}