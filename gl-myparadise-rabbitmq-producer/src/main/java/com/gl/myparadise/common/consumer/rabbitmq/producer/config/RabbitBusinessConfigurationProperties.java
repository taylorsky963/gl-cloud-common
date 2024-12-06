package com.gl.myparadise.common.consumer.rabbitmq.producer.config;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: RabbitMQListenerConfigurationProperties
 * @ProjectName: gl-myparadise-common
 * @Date 2023-05-30 - 11:31
 */
public class RabbitBusinessConfigurationProperties {

    private List<String> names;

    private List<RabbitExchangeProperties> exchanges = new ArrayList<RabbitExchangeProperties>(){};

    private List<RabbitQueueProperties> queues = new ArrayList<RabbitQueueProperties>(){};

    private List<RabbitBusinessProperties> businesses = new ArrayList<RabbitBusinessProperties>(){};

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }


    public List<RabbitExchangeProperties> getExchanges() {
        return exchanges;
    }

    public void setExchanges(List<RabbitExchangeProperties> exchanges) {
        this.exchanges = exchanges;
    }

    public List<RabbitQueueProperties> getQueues() {
        return queues;
    }

    public void setQueues(List<RabbitQueueProperties> queues) {
        this.queues = queues;
    }

    public List<RabbitBusinessProperties> getBusinesses() {
        return businesses;
    }

    public void setBusinesses(List<RabbitBusinessProperties> businesses) {
        this.businesses = businesses;
    }

}
