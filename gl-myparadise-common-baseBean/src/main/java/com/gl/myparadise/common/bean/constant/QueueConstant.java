package com.gl.myparadise.common.bean.constant;

public class QueueConstant {
    public final static String GLOBAL_EVENT_SAVE_TO_ELASTICSEARCH = "global-event-save-to-elasticsearch";
    public final static String GLOBAL_USER_SAVE_TO_ELASTICSEARCH = "global-user-save-to-elasticsearch";
    public final static String GLOBAL_USER_OTHER_SAVE_TO_ELASTICSEARCH = "global-user-other-save-to-elasticsearch";
    public final static String GLOBAL_SYSTEM_OPERATION_LOG_SAVE_TO_ELASTICSEARCH = "global-system-operation-log-save-to-elasticsearch";
    public final static String GLOBAL_FUND = "global-fund";
    public final static String GLOBAL_FUND_DIVERSION = "global-fund-diversion";
    public final static String GLOBAL_ACTIVITY = "global-activity";
    public final static String GLOBAL_ACTIVITY_EVENT = "global-activity-event";

    public final static String EVENT_DATE_COLLECTION = "global-event-date-collection";
    public final static String EVENT_DISPSET_COLLECTION = "global-event-dispset-collection";

    /**
     * 邀请好友报表：资金/优惠事件队列（CB7REQ-280）。
     * 与 GLOBAL_EVENT_SAVE_TO_ELASTICSEARCH 绑同一批 routingKey，各消费各的，互不影响。
     */
    public final static String EVENT_INVITE_REPORT = "global-event-invite-report";
    /** 上面那条队列的死信交换机与死信队列，用于失败延迟重投 */
    public final static String EVENT_INVITE_REPORT_DLX = "global-event-invite-report.dlx";
    public final static String EVENT_INVITE_REPORT_DLQ = "global-event-invite-report.dlq";

    public final static String GLOBAL_EVENT_BATCH_SAVE_TO_ELASTICSEARCH = "global-event-batch-save-to-elasticsearch";

    public final static String GLOBAL_ACTIVITY_BATCH_EVENT = "global-activity-batch-event";
}
