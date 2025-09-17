package com.gl.myparadise.common.consumer.rabbitmq.producer.service.impl;

import com.gl.myparadise.common.bean.constant.ProjectConstant;
import com.gl.myparadise.common.bean.model.ReportEvent;
import com.gl.myparadise.common.consumer.rabbitmq.producer.producer.ReportProducer;
import com.gl.myparadise.common.consumer.rabbitmq.producer.service.ReportRetryService;
import com.gl.myparadise.common.tools.bean.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.security.MessageDigest;

@Slf4j
@Component
@EnableScheduling
@ConditionalOnProperty(name = "sharding.jdbc.datasource.master.url")
public class ReportRetryServiceImpl implements ReportRetryService {

    @Autowired
    @Qualifier("rabbitmqProducerDataSource")
    private DataSource dataSource;

    @Autowired
    private ReportProducer reportProducer;

    @PostConstruct
    public void init() {
        startDatabaseWorker();
        log.info("ReportRetryServiceImpl 初始化完成，数据库工作线程已启动");
    }

    // 数据库操作队列，最大100个任务
    private final BlockingQueue<DatabaseTask> databaseQueue = new ArrayBlockingQueue<>(100);

    // 单线程执行器，用于处理数据库操作
    private final ExecutorService databaseExecutor = Executors.newSingleThreadExecutor(r -> {
        Thread thread = new Thread(r, "ReportRetry-DatabaseWorker");
        thread.setDaemon(true);
        return thread;
    });

    // 数据库任务接口
    @FunctionalInterface
    private interface DatabaseTask {
        void execute() throws SQLException;
    }

    /**
     * 启动数据库工作线程
     */
    private void startDatabaseWorker() {
        databaseExecutor.submit(() -> {
            log.info("数据库工作线程已启动");
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    DatabaseTask task = databaseQueue.take(); // 阻塞等待任务
                    task.execute();
                } catch (InterruptedException e) {
                    log.info("数据库工作线程被中断");
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception e) {
                    log.error("执行数据库任务时发生异常: ", e);
                }
            }
            log.info("数据库工作线程已停止");
        });
    }

    /**
     * 生成消息哈希值
     *
     * @param event   事件类型
     * @param message 消息对象
     * @return SHA-256 哈希值
     */
    private String generateMessageHash(ReportEvent event, Object message) {
        try {
            String content = event.name() + ":" + JsonUtil.toJson(message);
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(content.getBytes("UTF-8"));

            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            log.error("生成消息哈希失败: ", e);
            // 如果哈希生成失败，使用时间戳作为后备方案
            return String.valueOf(System.currentTimeMillis());
        }
    }

    @Override
    public void reportRetry(ReportEvent event, Object message) {
        if (event == null || message == null) {
            log.info("reportRetry: event or message is null, event: {}, message: {}", event, message);
            return;
        }

        String json = JsonUtil.toJson(message);
        String className = message.getClass().getName();
        String eventName = event.name();
        log.info("上报事件异常, 入库重新处理 {} {}", eventName, json);
        try {
            String messageHash = generateMessageHash(event, message);
            // 创建数据库任务
            DatabaseTask task = () -> {
                String sql = "INSERT IGNORE INTO reportErrorLog (event_type, message_content, message_class, message_hash, status, error_message, create_time) VALUES (?, ?, ?, ?, ?, ?, ?)";

                try (Connection conn = dataSource.getConnection();
                     PreparedStatement ps = conn.prepareStatement(sql)) {

                    // 设置参数
                    ps.setString(1, eventName);                                    // event_type
                    ps.setString(2, json);                                         // message_content
                    ps.setString(3, className);                                     // message_class
                    ps.setString(4, messageHash);                                  // message_hash
                    ps.setInt(5, 0);                                               // status (0-待开始)
                    ps.setString(6, null);                                         // error_message
                    ps.setTimestamp(7, java.sql.Timestamp.valueOf(LocalDateTime.now())); // create_time

                    int insertResult = ps.executeUpdate();

                    if (insertResult > 0) {
                        log.info("报告日志已记录到数据库，事件类型: {}, 哈希: {}", eventName, messageHash);
                    } else {
                        log.debug("报告日志已存在，跳过重复插入，事件类型: {}, 哈希: {}", eventName, messageHash);
                    }
                }
            };

            // 尝试将任务加入队列
            try {
                boolean offered = databaseQueue.offer(task, 1, TimeUnit.SECONDS);
                if (!offered) {
                    log.warn("数据库队列已满，丢弃任务，事件类型: {}", eventName);
                }
            } catch (InterruptedException e) {
                log.error("等待队列空间时被中断，事件类型: {}", eventName, e);
                Thread.currentThread().interrupt();
            }
        } catch (Exception e) {
            log.error("上报重试失败, {}, {} {}", eventName, className, json, e);
        }
    }

//    @PostConstruct
//    public void initReportLog() {
//        ActivityMemberAwardReport report = new ActivityMemberAwardReport();
//        report.setAwardType(15);
//        report.setPrizeId(15);
//        report.setPrizeName("积分1");
//        report.setPrice(1.0);
//        report.setStatus(1);
//        report.setCreateTime(new Date(1758002768544L));
//        report.setClaimTime(new Date(1758002768544L));
//        HashMap<Object, Object> objectObjectHashMap = Maps.newHashMap();
//        report.setExpireTime(new Date(1758866768542L));
//        report.setExtraInfo(objectObjectHashMap.put("vip", 0)); // 若有使用 Jackson 可考慮轉換為 JsonNode
//        report.setUserAccount("m140551zhmprj");
//        report.setActivityDataPkCode("102ACTIVITYVIPCODE99999");
//        report.setActName("VIP奖励");
//        report.setActTemplate("");
//        report.setUsername("m140551zhmprj");
//        report.setDesireMultiple(0);
//        report.setSender("sys");
//        report.setTradeId("806tqRHYdrLHDvJeLc80608");
//        report.setSubType(3009);
//        report.setMemberInfoId(17542L);
//        report.setAgentId(-100L);
//
//// 其他欄位若無資料可不設，或視需求補上：
//        report.setImgUrl(null);
//        report.setTelephone(null);
//        report.setParentId(null);
//        report.setParentName(null);
//        report.setRemark(null);
//        report.setWithdrewOrderId(null);
//        report.setApplyRemark(null);
//        report.setValidDays(null);
//        report.setFirstVerifyRemark(null);
//        report.setSecondVerifyRemark(null);
//        report.setUserTag(null);
//        report.setCoinCode(null);
//        report.setCoinDataPkCode(null);
//
//        reportProducer.sendMessage(ReportEvent.Recharge, report);
//    }

    /**
     * 扫描待开始的任务并发送消息
     * 每5分钟执行一次
     */
    @Scheduled(fixedRate = ProjectConstant.Common.MILLISECOND.MINUTE * 5)
    public void scanAndSendPendingReports() {
        List<ReportLogRecord> pendingReports = getPendingReports();

        if (pendingReports.isEmpty()) {
            log.info("没有待处理的任务");
            return;
        }

        log.info("发现 {} 个待处理的任务", pendingReports.size());

        for (ReportLogRecord record : pendingReports) {
            try {
                ReportEvent event = ReportEvent.valueOf(record.getEventType());
                Class<?> messageClass = Class.forName(record.getMessageClass());
                Object bean = JsonUtil.toBean(record.getMessageContent(), messageClass);

                log.info("准备发送消息，事件类型: {}, 消息类: {}, 消息内容: {}", event, record.getMessageClass(), record.getMessageContent());

                if (bean instanceof com.gl.myparadise.common.bean.report.BaseReport) {
                    reportProducer.sendMessage(event, (com.gl.myparadise.common.bean.report.BaseReport) bean);
                } else {
                    log.warn("消息类型不是 BaseReport，跳过发送，ID: {}, 类型: {}", record.getId(), record.getMessageClass());
                    updateStatusToFailed(record.getId(), "消息类型不是 BaseReport: " + record.getMessageClass());
                    continue;
                }

                updateStatusToSuccess(record.getId());
                log.info("任务处理成功，ID: {}, 事件类型: {}", record.getId(), record.getEventType());
            } catch (Exception e) {
                log.error("处理任务失败，ID: {}, 事件类型: {}", record.getId(), record.getEventType(), e);
                updateStatusToFailed(record.getId(), e.getMessage());
            }
        }
    }

    /**
     * 获取待处理的任务列表
     */
    private List<ReportLogRecord> getPendingReports() {
        String sql = "SELECT id, event_type, message_content, message_class FROM reportErrorLog WHERE status = 0 ORDER BY create_time ASC LIMIT 100";
        List<ReportLogRecord> records = new ArrayList<>();

        try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ReportLogRecord record = new ReportLogRecord();
                record.setId(rs.getLong("id"));
                record.setEventType(rs.getString("event_type"));
                record.setMessageContent(rs.getString("message_content"));
                record.setMessageClass(rs.getString("message_class"));
                records.add(record);
            }

        } catch (SQLException e) {
            log.error("查询待处理任务失败: ", e);
        }

        return records;
    }

    /**
     * 更新状态为成功（通过ID）
     */
    private void updateStatusToSuccess(Long id) {
        DatabaseTask task = () -> {
            String updateSql = "UPDATE reportErrorLog SET status = 1 WHERE id = ?";

            try (Connection conn = dataSource.getConnection();
                 PreparedStatement ps = conn.prepareStatement(updateSql)) {

                ps.setLong(1, id);
                int updateResult = ps.executeUpdate();

                if (updateResult > 0) {
                    log.debug("状态已更新为成功，ID: {}", id);
                }
            }
        };

        try {
            boolean offered = databaseQueue.offer(task, 1, TimeUnit.SECONDS);
            if (!offered) {
                log.warn("数据库队列已满，无法更新状态，ID: {}", id);
            }
        } catch (InterruptedException e) {
            log.error("等待队列空间时被中断，ID: {}", id, e);
            Thread.currentThread().interrupt();
        }
    }

    /**
     * 更新状态为失败
     */
    private void updateStatusToFailed(Long id, String errorMessage) {
        DatabaseTask task = () -> {
            String updateSql = "UPDATE reportErrorLog SET status = 0, error_message = ? WHERE id = ?";

            try (Connection conn = dataSource.getConnection();
                 PreparedStatement ps = conn.prepareStatement(updateSql)) {

                ps.setString(1, errorMessage);
                ps.setLong(2, id);
                int updateResult = ps.executeUpdate();

                if (updateResult > 0) {
                    log.debug("状态已更新为失败，ID: {}", id);
                }
            }
        };

        try {
            boolean offered = databaseQueue.offer(task, 1, TimeUnit.SECONDS);
            if (!offered) {
                log.warn("数据库队列已满，无法更新状态，ID: {}", id);
            }
        } catch (InterruptedException e) {
            log.error("等待队列空间时被中断，ID: {}", id, e);
            Thread.currentThread().interrupt();
        }
    }

    /**
     * 报告日志记录内部类
     */
    private static class ReportLogRecord {
        private Long id;
        private String eventType;
        private String messageContent;
        private String messageClass;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getEventType() {
            return eventType;
        }

        public void setEventType(String eventType) {
            this.eventType = eventType;
        }

        public String getMessageContent() {
            return messageContent;
        }

        public void setMessageContent(String messageContent) {
            this.messageContent = messageContent;
        }

        public String getMessageClass() {
            return messageClass;
        }

        public void setMessageClass(String messageClass) {
            this.messageClass = messageClass;
        }
    }
}
