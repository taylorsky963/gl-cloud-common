package com.gl.myparadise.common.bean.constant;

/**
 * 项目相关常量定义类。
 */
public final class ProjectConstant {

    public static final class DatabaseLogicPk {
        public final static String GL_ACTIVITY_MANUAL_SEND_RECORD = "100";
        public final static String GL_ACTIVITY_MANUAL_SEND_USER = "101";
        public final static String GL_ACTIVITY = "102";
        public final static String GL_ACTIVITY_TYPE = "103";
        public final static String GL_ACTIVITY_TRANSACTION_LOG = "104";
    }

    public static final class Common {
        /**
         * 否 (FALSE)
         */
        public final static String FALSE = "0";

        /**
         * 是 (TRUE)
         */
        public final static String TRUE = "1";

        public static class SECOND {

            /**
             * 一分钟的秒数
             */
            public static final long MINUTE = 60;

            /**
             * 一小时的秒数
             */
            public static final long HOUR = 3600;

            /**
             * 一天的秒数
             */
            public static final long DAY = 86400;

            /**
             * 一周的秒数
             */
            public static final long WEEK = 604800;

            /**
             * 一年的秒数 (按365天计算)
             */
            public static final long YEAR = 31536000;
        }
    }

    /**
     * 流水号前缀相关常量
     */
    public static final class TradeNo {

        /**
         * 活动奖励流水号前缀
         */
        public final static String ACTIVITY_AWARD_PREFIX = "AA";
    }

    /**
     * 活动相关常量
     */
    public static final class Activity {


        public static final class AwardType {
            /**
             * 活动奖励类型：现金奖励
             */
            public final static int ACTIVITY_CASH = -9;

            /**
             * 活动奖励类型：积分奖励
             */
            public final static int ACTIVITY_POINT = 15;
        }

        public static final class AwardStatus {
            /**
             * 状态：不可用
             */
            public final static int UN_AVAILABLE = -1;
            /**
             * 状态：可用
             */
            public final static int AVAILABLE = 0;
            /**
             * 状态：已领取
             */
            public final static int RECEIVED = 1;
            /**
             * 状态：已过期
             */
            public final static int EXPIRED = 2;
            /**
             * 状态：已发送
             */
            public final static int ALREADY_SEND = 3;
            /**
             * 状态：已拒绝
             */
            public final static int ALREADY_REFUSAL = 4;
            /**
             * 状态：已删除
             */
            public final static int DELETE = 5;
            /**
             * 状态：未使用
             */
            public final static int UN_USE = 6;
        }

        public static final class SubType {
            public final static int SUB_TYPE_1 = 1;
            public final static int SUB_TYPE_2 = 2;
            public final static int SUB_TYPE_3 = 3;
            public final static int SUB_TYPE_4 = 4;
            public final static int SUB_TYPE_5 = 5;
            public final static int SUB_TYPE_6 = 6;
            public final static int SUB_TYPE_7 = 7;
            public final static int SUB_TYPE_8 = 8;
            public final static int SUB_TYPE_9 = 9;
            public final static int SUB_TYPE_10 = 10;
            public final static int SUB_TYPE_11 = 11;
            public final static int SUB_TYPE_12 = 12;
            public final static int SUB_TYPE_13 = 13; // VIP升级礼金
            public final static int SUB_TYPE_14 = 14;
        }

        public static final class ProcessStatus {

            /**
             * 未开始: 表示此流程尚未开始处理
             */
            public final static int NOT_STARTED = 0;

            /**
             * 进行中: 表示此流程正在进行处理
             */
            public final static int IN_PROGRESS = 1;

            /**
             * 已完成: 表示此流程已完成处理
             */
            public final static int COMPLETED = 2;
            /**
             * 失敗
             */
            public final static int FAIL = 3;
        }

        public static final class TemplateType {

            /**
             * 空模板
             */
            public final static String EMPTY_TEMPLATE = "emptyTemplate";
            /**
             * 自定义模板
             */
            public final static String CUSTOM_TEMPLATE = "customTemplate";
            /**
             * 投注返水模板
             */
            public final static String REBATE_TEMPLATE = "rebateTemplate";
            /**
             * 时时返水模板
             */
            public final static String REBATE_REALTIME_TEMPLATE = "rebateRealtimeTemplate";
        }

        /**
         * MQ 任务处理相关常量
         */
        public static final class MQHandler {
            /**
             * 手动发送 MQ
             */
            public final static String MANUAL_SEND = "MANUAL_SEND";
        }

        public static final class LockKey {

            public final static String ACTIVITY_MANUAL_SEND_MQ_LOCK_KEY = "ACTIVITY_MANUAL_SEND_MQ_LOCK_KEY_";
            public final static String ACTIVITY_MANUAL_SEND_LOCK_KEY = "ACTIVITY_MANUAL_SEND_LOCK_KEY_";
        }
    }


    /**
     * 游戏相关常量
     */
    public static final class Game {
        public static final class BetflowStatus {

        }
    }

    /**
     * 资金相关常量
     */
    public static final class Fund {
        public static final class BalanceType {
            /**
             * 帐变资金类型：充值
             */
            public final static int RECHARGE = 1;
            /**
             * 帐变资金类型：提现
             */
            public final static int WITHDRAW = 2;
            /**
             * 帐变资金类型：转账
             */
            public final static int TRANSFER = 3;
            /**
             * 帐变资金类型：红利
             */
            public final static int BONUS = 4;
            /**
             * 帐变资金类型：返水
             */
            public final static int REBATE = 5;
        }


        public static final class ServiceType {
            /**
             * 遊戲轉帳
             */
            public final static String GAME = "7001";
        }
    }
}
