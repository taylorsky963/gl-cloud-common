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
        public final static String GL_ACTIVITY_REALTIME_REBATE_RECORD = "105";
        public final static String GL_ACTIVITY_REBATE_RECORD = "106";
        public final static String GL_ACTIVITY_USER_REGISTER_RECORD = "107";
        public final static String GL_ACTIVITY_DAILY_FIRST_RECHARGE_RECORD = "108";
        public final static String GL_ACTIVITY_RECHARGE_RECORD = "109";
        public final static String GL_ACTIVITY_LOGIN_RECHARGE_PENDING_RECORD = "110";
        public final static String GL_ACTIVITY_USER_INFO_BINDING_RECORD = "114";
        public final static String GL_ACTIVITY_REDEMPTION_CODE = "111";
        public final static String GL_ACTIVITY_REDEMPTION_CODE_BATCH = "112";
        public final static String GL_ACTIVITY_REDEMPTION_CODE_DETAIL = "113";
        public final static String GL_ACTIVITY_INVITE_RECORD = "114";
        public final static String GL_ACTIVITY_INVITE_ACHIEVEMENT_RECORD = "115";
        public final static String GL_ACTIVITY_FIRST_DEPOSIT_RECORD = "117";
    }

    public static final class WebsiteConfigKey {
        public final static String POINTS_EXPIRATION_DAYS = "pointsExpirationDays";

    }

    public static final class AGENT {
        public final static Long DEFAULT_AGENT_ID = -100L;
    }

    public static final class Common {

        public final static String TWO_MIN_CACHE = "TWO_MIN_CACHE";

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

        public static class MILLISECOND {
            /**
             * 一分钟的毫秒数 (60,000 ms)
             */
            public static final long MINUTE = SECOND.MINUTE * 1000;

            /**
             * 一小时的毫秒数 (3,600,000 ms)
             */
            public static final long HOUR = SECOND.HOUR * 1000;

            /**
             * 一天的毫秒数 (86,400,000 ms)
             */
            public static final long DAY = SECOND.DAY * 1000;

            /**
             * 一周的毫秒数 (604,800,000 ms)
             */
            public static final long WEEK = SECOND.WEEK * 1000;

            /**
             * 一年的毫秒数 (31,536,000,000 ms，按365天计算)
             */
            public static final long YEAR = SECOND.YEAR * 1000;
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

            /**
             * 活动奖励类型：大轉盤參與券
             */
            public final static int LUCKY_SPIN_TICKET = 16;
        }

        public static final class ReceiveMode {
            public final static int AUTO = 0;
            public final static int MANUAL = 1;
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
            /**
             * 注册优惠模板
             */
            public final static String REGISTER_TEMPLATE = "registerTemplate";
            /**
             * 每日首充模板
             */
            public final static String DAILY_FIRST_RECHARGE_TEMPLATE = "dailyFirstRechargeTemplate";
            /**
             * 充值优惠模板
             */
            public final static String RECHARGE_TEMPLATE = "rechargeTemplate";
            /**
             * 登入充值抽奖模板
             */
            public final static String LOGIN_RECHARGE_TEMPLATE = "loginRechargeTemplate";

            /**
             * 大轉盤模板
             */
            public final static String LUCKY_SPIN_TEMPLATE = "luckySpinTemplate";

            /**
             * 笔笔存优惠模板
             */
            public final static String EVERY_RECHARGE_TEMPLATE = "everyRechargeTemplate";

            /**
             * 邀请好友模板
             */
            public final static String INVITE_TEMPLATE = "inviteTemplate";

            /**
             * 信息绑定优惠模板
             */
            public final static String INFO_BINDING_TEMPLATE = "infoBindingTemplate";

            /**
             * 首存优惠模板
             */
            public final static String FIRST_DEPOSIT_TEMPLATE = "firstDepositTemplate";

            /**
             * 新返水优惠模板
             */
            public final static String NEW_REBATE_TEMPLATE = "newRebateTemplate";
        }

        public static final class ActivityDataPkCode {

            /**
             * VIP
             */
            public final static String VIP = "102ACTIVITYVIPCODE99999";

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
            public final static String ACTIVITY_WEBSITE_CONFIG_KEY = "ACTIVITY_WEBSITE_CONFIG_KEY_";
            public final static String ACTIVITY_CURRENCY_CONFIG_KEY = "ACTIVITY_CURRENCY_CONFIG_KEY_";
        }
    }

    /**
     * 活动相关常量
     */
    public static final class Data {
        public static final class LockKey {
            public final static String DATA_CURRENCY_CONFIG_KEY = "DATA_CURRENCY_CONFIG_KEY_";
            public final static String DATA_CBO_MAPPING_KEY = "DATA_CBO_MAPPING_KEY_";
            public final static String DATA_GAMELIST_MAPPING_KEY = "DATA_GAMELIST_MAPPING_KEY_";
            public final static String AGENT_CACHE_KEY_PREFIX = "DATA_AGENT_CONFIG_KEY_";
        }
    }

    /**
     * 游戏相关常量
     */
    public static final class Game {
        public static final class LockKey {

            public final static String GAME_BETTING_JOB_LOCK_KEY = "GAME_BETTING_JOB_LOCK_KEY_";
        }
    }

    /**
     * 资金相关常量
     */
    public static final class Fund {
        public static final class BalanceType { // transactionType
            /**
             * 帐变资金类型：充值
             */
            public final static int RECHARGE = 1;
            /**
             * 帐变资金类型：提现
             */
            public final static int WITHDRAW = 2;
            /**
             * 帐变资金类型：红利
             */
            public final static int BONUS = 3;
            /**
             * 帐变资金类型：返水
             */
            public final static int REBATE = 4;
            /**
             * 帐变资金类型：人工
             */
            public final static int MANUAL = 5;
            /**
             * 帐变资金类型： 游戏帐变
             */
            public final static int GAME = 6;
            /**
             * 帐变资金类型： 积分兑换
             */
            public final static int POINT = 7;

            /**
             * 帐变资金类型： 參與券
             */
            public final static int TICKET = 8;
        }

        public static final class TransIo { // transactionType
            /**
             * 增加
             */
            public final static int Add = 1;
            /**
             * 減少
             */
            public final static int SUB = -1;
        }

        public static final class ServiceType {
            /**
             * 遊戲轉帳
             */
            public final static String GAME = "7001";

            /**
             * 财务转帐
             */
            public final static String FINANCIAL = "6001";
            /**
             * 启台会员人工轉帳
             */
            public final static String ADMIN_MEMBER = "8001";
            /**
             * 活動
             */
            public final static String ACTIVITY = "3001";
        }

        public static final class PayOrder {
            //会员支付订单状态：创建订单
            public final static String PAYMENT_ORDER_STATUS_CREATE = "70008001";
            //会员支付订单状态：订单回调成功，订单状态修改
            public final static String PAYMENT_ORDER_STATUS_SUCCESS = "70008002";
            //会员支付订单状态：订单超时过期
            public final static String PAYMENT_ORDER_STATUS_EXPIRED = "70008003";
            //会员支付订单状态：无效订单
            public final static String PAYMENT_ORDER_STATUS_INVALID = "70008004";
        }

        public static final class WithdrawOrder {
            //提款订单创建状态
            public final static String WITHDRAW_ORDER_STATUS_CREATE = "70015001";
            //提款订单风控拒绝
            public final static String WITHDRAW_ORDER_STATUS_RISK = "70015004";
            //提款订单等待财务出款
            public final static String WITHDRAW_ORDER_STATUS_FINANCIAL = "70015002";
            //提款订单出款成功
            public final static String WITHDRAW_ORDER_STATUS_SUCCESS = "70015003";
            //提款订单发起代付请求后的等待三方回调
            public final static String WITHDRAW_ORDER_STATUS_DFWAITING = "70015005";
        }
    }
}
