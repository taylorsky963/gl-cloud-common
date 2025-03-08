package com.gl.myparadise.common.bean.report.activity;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityBaseReport implements Serializable {

    private String event;
}