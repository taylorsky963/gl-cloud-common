package com.gl.myparadise.common.consumer.rabbitmq.producer.service;

import com.gl.myparadise.common.bean.report.user.LoginReport;
import com.gl.myparadise.common.bean.report.user.RegisterReport;
import com.gl.myparadise.common.bean.report.user.UserSync;

public interface ReportService {

    /**
     * 登录上报
     */
    void loginReport(LoginReport loginReport);

    /**
     * 注册上报
     */
    void registerReport(RegisterReport registerReport);

    /**
     * 用户信息同步上报
     */
    void userSync(UserSync userSync);
}
