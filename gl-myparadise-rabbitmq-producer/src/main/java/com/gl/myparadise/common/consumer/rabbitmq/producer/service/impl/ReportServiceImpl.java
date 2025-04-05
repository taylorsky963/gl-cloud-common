package com.gl.myparadise.common.consumer.rabbitmq.producer.service.impl;

import com.gl.myparadise.common.bean.model.ReportEvent;
import com.gl.myparadise.common.bean.report.activity.ActivityBaseReport;
import com.gl.myparadise.common.bean.report.fund.FundBaseReport;
import com.gl.myparadise.common.bean.report.fund.RechargeReport;
import com.gl.myparadise.common.bean.report.fund.WithdrawReport;
import com.gl.myparadise.common.bean.report.system.SystemOperationLogReport;
import com.gl.myparadise.common.bean.report.user.*;
import com.gl.myparadise.common.consumer.rabbitmq.producer.producer.DefaultProducer;
import com.gl.myparadise.common.consumer.rabbitmq.producer.producer.ReportProducer;
import com.gl.myparadise.common.consumer.rabbitmq.producer.producer.UserProducer;
import com.gl.myparadise.common.consumer.rabbitmq.producer.service.ReportService;
import com.gl.myparadise.common.tools.bean.JsonUtil;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;

@Slf4j
@Component
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportProducer reportProducer;

    @Autowired
    private UserProducer userProducer;

    @Autowired
    private DefaultProducer defaultProducer;

    @Override
    public void loginReport(LoginReport loginReport) {
        reportProducer.sendMessage(ReportEvent.Login, loginReport);
    }

    @Override
    public void registerReport(RegisterReport registerReport) {
        reportProducer.sendMessage(ReportEvent.Register, registerReport);
    }

    @Override
    public void userSync(@NonNull UserSync userSync) {
        log.info("用户上报 {}", JsonUtil.toJson(userSync));
        userProducer.sendMessage(userSync);
    }

    @Override
    public void systemOperationReport(SystemOperationLogReport logReport) {
        defaultProducer.systemOperationReport(logReport);
    }

    @Override
    public void bettingReport(BettingReport report) {
        report.set("uuid", Objects.requireNonNull(report.getFlowId()));
        reportProducer.sendMessage(ReportEvent.Betting, report);
    }

    @Override
    public void gameTransferReport(GameTransferReport report) {
        report.set("uuid", UUID.randomUUID().toString());
        reportProducer.sendMessage(ReportEvent.GameTransfer, report);
    }

    @Override
    public void fundReport(FundBaseReport report) {
        report.setTimestamp(System.currentTimeMillis());
        defaultProducer.sendFundReport(report);
    }

    @Override
    public void activityReport(ActivityBaseReport report) {
        defaultProducer.sendActivity(report);
    }

    @Override
    public void userWalletReport(UserWalletReport report) {
        reportProducer.sendMessage(ReportEvent.UserWallet, report);
    }

    @Override
    public void rechargeReport(RechargeReport report) {
        report.set("uuid", report.getOrderNumber());
        reportProducer.sendMessage(ReportEvent.Recharge, report);
    }

    @Override
    public void withdrawReport(WithdrawReport report) {
        report.set("uuid", report.getOrderNumber());
        reportProducer.sendMessage(ReportEvent.Withdraw, report);
    }

    @Override
    public void memberVipReport(MemberVipReport report) {
        report.set("uuid", report.getDataPkCode());
        reportProducer.sendMessage(ReportEvent.MemberVip, report);
    }

    @Override
    public void dailyUserBettingReport(DailyUserBettingReport report) {
        reportProducer.sendMessage(ReportEvent.DailyUserBetting, report);
    }

    @Override
    public void dailyUserRechargeReport(DailyUserRechargeReport report) {
        reportProducer.sendMessage(ReportEvent.DailyUserRecharge, report);
    }

    @Override
    public void balanceChangeReport(BalanceChangeReport report) {
        report.set("uuid", report.getDataPkCode());
        reportProducer.sendMessage(ReportEvent.BalanceChange, report);
    }

    @Override
    public void pointChangeReport(PointChangeReport report) {
        report.set("uuid", report.getDataPkCode());
        reportProducer.sendMessage(ReportEvent.PointChange, report);
    }

    @Override
    public void betFlowChangeReport(BetFlowChangeReport report) {
        report.set("uuid", report.getDataPkCode());
        reportProducer.sendMessage(ReportEvent.BetFlowChange, report);
    }
}
