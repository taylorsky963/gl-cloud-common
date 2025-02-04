package com.gl.myparadise.common.consumer.rabbitmq.producer.service;

import com.gl.myparadise.common.bean.report.system.SystemOperationLogReport;
import com.gl.myparadise.common.bean.report.user.*;

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

    /**
     * 后台操作日志上报
     */
    void systemOperationReport(SystemOperationLogReport logReport);

    /**
     * 游戏注单成功上报
     */
    void bettingReport(BettingReport report);

    /**
     * 游戏轉帳紀錄
     */
    void gameTransferReport(GameTransferReport report);
}
