package com.gl.myparadise.common.consumer.rabbitmq.producer.service;

import com.gl.myparadise.common.bean.report.activity.ActivityBaseReport;
import com.gl.myparadise.common.bean.report.activity.ActivityMemberAwardReport;
import com.gl.myparadise.common.bean.report.fund.FundBaseReport;
import com.gl.myparadise.common.bean.report.fund.RechargeReport;
import com.gl.myparadise.common.bean.report.fund.WithdrawReport;
import com.gl.myparadise.common.bean.report.system.SystemOperationLogReport;
import com.gl.myparadise.common.bean.report.user.*;

public interface ReportService {

    /**
     * 用户信息同步上报
     */
    void userSync(UserSync report);

    /**
     * 后台操作日志上报
     */
    void systemOperationReport(SystemOperationLogReport report);

    /**
     * 游戏注单成功上报
     */
    void bettingReport(BettingReport report);

    /**
     * 财务相关上报
     */
    void fundReport(FundBaseReport report);

    /**
     * 活动上报
     */
    void activityReport(ActivityBaseReport report);

    /**
     * 用户钱包建立上报
     */
    void userWalletReport(UserWalletReport report);

    /**
     * 充值纪录上报
     */
    void rechargeReport(RechargeReport report);

    /**
     * 提現紀錄上報
     */
    void withdrawReport(WithdrawReport report);

    /**
     * 用戶VIP上報
     */
    void memberVipReport(MemberVipReport report);

    /**
     * 每日用户投注数据
     */
    void dailyUserBettingReport(DailyUserBettingReport report);

    /**
     * 每日用户充值数据
     */
    void dailyUserRechargeReport(DailyUserRechargeReport report);

    /**
     * 用户余额变动上报
     */
    void balanceChangeReport(BalanceChangeReport report);

    /**
     * 用户积分变动上报
     */
    void pointChangeReport(PointChangeReport report);

    /**
     * 用户流水变动上报
     */
    void betFlowChangeReport(BetFlowChangeReport report);

    /**
     * 活動领奖纪录上报
     */
    void activityMemberAward(ActivityMemberAwardReport report);
}
