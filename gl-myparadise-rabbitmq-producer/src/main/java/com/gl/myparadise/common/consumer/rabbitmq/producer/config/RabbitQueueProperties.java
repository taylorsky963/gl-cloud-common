package com.gl.myparadise.common.consumer.rabbitmq.producer.config;

import java.util.Map;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: RabbitQueueProperties
 * @ProjectName: gl-myparadise-common
 * @Date 2023-05-26 - 0:52
 */
public class RabbitQueueProperties {

    /**
     * 队列名（每个队列的名称应该唯一）
     * 必须*
     */
    private String queuesName;

    /**
     * 指定绑定交互机，可绑定多个（逗号分隔）
     * 必须*
     */
    private String exchangeName;

    /**
     * 队列路由键（队列绑定交换机的匹配键，例如：“user” 将会匹配到指定路由器下路由键为：【*.user、#.user】的队列）
     */
    private String routingkey;

    /**
     * 是否为持久队列（该队列将在服务器重启后保留下来）
     */
    private Boolean durable = Boolean.TRUE;

    /**
     * 是否为排它队列
     */
    private Boolean exclusive = Boolean.FALSE;

    /**
     * 如果队列为空是否删除（如果服务器在不使用队列时是否删除队列）
     */
    private Boolean autoDelete = Boolean.FALSE;

    /**
     * 头队列是否全部匹配
     * 默认：是
     */
    private Boolean whereAll = Boolean.TRUE;

    /**
     * 参数
     */
    private Map<String, Object> args;

    /**
     * 消息头
     */
    private Map<String, Object> headers;

    public String getQueuesName() {
        return queuesName;
    }

    public void setQueuesName(String queuesName) {
        this.queuesName = queuesName;
    }

    public String getExchangeName() {
        return exchangeName;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }

    public String getRoutingkey() {
        return routingkey;
    }

    public void setRoutingkey(String routingkey) {
        this.routingkey = routingkey;
    }

    public Boolean getDurable() {
        return durable;
    }

    public void setDurable(Boolean durable) {
        this.durable = durable;
    }

    public Boolean getExclusive() {
        return exclusive;
    }

    public void setExclusive(Boolean exclusive) {
        this.exclusive = exclusive;
    }

    public Boolean getAutoDelete() {
        return autoDelete;
    }

    public void setAutoDelete(Boolean autoDelete) {
        this.autoDelete = autoDelete;
    }

    public Boolean getWhereAll() {
        return whereAll;
    }

    public void setWhereAll(Boolean whereAll) {
        this.whereAll = whereAll;
    }

    public Map<String, Object> getArgs() {
        return args;
    }

    public void setArgs(Map<String, Object> args) {
        this.args = args;
    }

    public Map<String, Object> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, Object> headers) {
        this.headers = headers;
    }

}
