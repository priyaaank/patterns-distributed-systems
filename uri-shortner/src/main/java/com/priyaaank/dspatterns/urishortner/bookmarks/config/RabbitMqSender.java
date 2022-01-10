package com.priyaaank.dspatterns.urishortner.bookmarks.config;

import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class RabbitMqSender {

    public static final String QUEUE_MESSAGE_COUNT = "QUEUE_MESSAGE_COUNT";
    private final RabbitTemplate rabbitTemplate;
    private final RabbitAdmin rabbitAdmin;
    private Integer maxQueueSize;
    private Boolean queueRejectEnabled;

    @Autowired
    public RabbitMqSender(RabbitTemplate rabbitTemplate,
                          RabbitAdmin rabbitAdmin,
                          @Value("${config.queue.max.size}") Integer maxQueueSize,
                          @Value("${config.queue.reject.enabled}") Boolean queueRejectEnabled) {
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitAdmin = rabbitAdmin;
        this.maxQueueSize = maxQueueSize;
        this.queueRejectEnabled = queueRejectEnabled;
    }

    public void sendMessage(String message) {
        Properties queueProperties = rabbitAdmin.getQueueProperties(RabbitMqConfig.queueName);
        int queueSize = Integer.parseInt(queueProperties.get(QUEUE_MESSAGE_COUNT).toString());
        if (queueRejectEnabled) throwErrorIfSizeThresholdReached(queueSize);
        this.rabbitTemplate.convertAndSend(RabbitMqConfig.exchangeName, RabbitMqConfig.routingKey, message);
    }

    private void throwErrorIfSizeThresholdReached(int queueSize) {
        if (queueSize >= this.maxQueueSize) throw new RuntimeException("Cannot accept more currently");
    }
}
